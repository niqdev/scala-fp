package com.github.niqdev.cats

import cats.data.State
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class StateSpec extends AnyWordSpecLike with Matchers {

  "State" should {

    "verify examples" in {
      /*
       * (1) transforms an input state to an output state
       * (2) computes a result
       */
      val myState = State[Int, String](state => (state, s"state=$state"))

      // run(initial state)
      // state and result
      myState.run(42).value shouldBe Tuple2(42, "state=42")
      // only state
      myState.runS(42).value shouldBe 42
      // only result
      myState.runA(42).value shouldBe "state=42"

      // extracts the state as the result
      State.get[Int].run(8).value shouldBe Tuple2(8, 8)
      // updates the state and returns unit as the result
      State.set[Int](8).run(999).value shouldBe Tuple2(8, ())
      // ignores the state and returns a supplied result
      State.pure[Int, String]("result").run(8).value shouldBe Tuple2(8, "result")
      // extracts the state via a transformation function
      State.inspect[Int, String](value => s"$value!").run(8).value shouldBe Tuple2(8, "8!")
      // updates the state using an update function
      State.modify[Int](_ + 1).run(7).value shouldBe Tuple2(8, ())
    }

    "verify composition (1)" in {
      val step1 = State[Int, String] { state =>
        val newState = state * 4
        val result   = s"[step1][before=$state][after$newState]"
        (newState, result)
      }

      val step2 = State[Int, String] { state =>
        val newState = state + 1
        val result   = s"[step2][before=$state][after$newState]"
        (newState, result)
      }

      val step3 = State[Int, String] { state =>
        val newState = state * 2
        val result   = s"[step3][before=$state][after$newState]"
        (newState, result)
      }

      val myState = for {
        s1 <- step1
        s2 <- step2
        s3 <- step3
      } yield (s1, s2, s3)

      val expectedState = 42
      val expectedResult = (
        "[step1][before=5][after20]",
        "[step2][before=20][after21]",
        "[step3][before=21][after42]"
      )
      myState.run(5).value shouldBe Tuple2(expectedState, expectedResult)
    }

    "verify composition (2)" in {
      import State._

      val program: State[Int, (Int, Int, Int)] = for {
        a <- get[Int]
        _ <- set[Int](a + 1)
        b <- get[Int]
        _ <- modify[Int](_ + 1)
        c <- inspect[Int, Int](_ * 1000)
      } yield (a, b, c)

      val (state, result) = program.run(1).value
      state shouldBe 3
      result shouldBe Tuple3(1, 2, 3000)
    }

    /**
      * (1 + 2) * 3)
      *
      * 1 2 + 3 * // see 1, push onto stack 2 + 3 * // see 2, push onto stack + 3 * // see +, pop 1 and 2 off
      * of stack, // push (1 + 2) = 3 in their place 3 3 * // see 3, push onto stack 3 * // see 3, push onto
      * stack * // see *, pop 3 and 3 off of stack, // push (3 * 3) = 9 in their place
      */
    "verify Post-Order Calculator" in {
      import cats.syntax.applicative.catsSyntaxApplicativeId

      type CalculatorState[A] = State[List[Int], A]

      def operand(value: Int): CalculatorState[Int] =
        State[List[Int], Int] { stack =>
          // push onto stack
          (value :: stack, value)
        }

      def operator(operation: (Int, Int) => Int): CalculatorState[Int] =
        State[List[Int], Int] {
          // pop v2 and v1 off of stack
          case v2 :: v1 :: tail =>
            val result = operation(v2, v1)
            // push result in their place
            (result :: tail, result)
          case _ =>
            ???
        }

      @scala.annotation.nowarn
      def evaluateOne(symbol: String): CalculatorState[Int] =
        symbol.trim match {
          case "+"   => operator(_ + _)
          case "*"   => operator(_ * _)
          case "-"   => operator(_ - _)
          case "/"   => operator(_ / _)
          case value =>
            // java.lang.NumberFormatException
            operand(value.toInt)
        }

      evaluateOne("42").runA(Nil).value shouldBe 42

      val program0 = for {
        _      <- evaluateOne("1")
        _      <- evaluateOne("2")
        result <- evaluateOne("+")
      } yield result

      program0.runA(Nil).value shouldBe 3

      def evaluateAll(input: List[String]): CalculatorState[Int] =
        input.foldLeft(0.pure[CalculatorState]) { case (state, symbol) =>
          state.flatMap(_ => evaluateOne(symbol))
        }

      evaluateAll(List("1", "2", "+", "3", "*")).runA(Nil).value shouldBe 9

      val program1 = for {
        _      <- evaluateAll(List("1", "2", "+"))
        _      <- evaluateAll(List("3", "4", "+"))
        result <- evaluateOne("*")
      } yield result

      program1.runA(Nil).value shouldBe 21

      def evaluateInput(input: String): CalculatorState[Int] =
        evaluateAll(input.split(" ").toList)

      val program2 = for {
        _      <- evaluateInput("1 2 +")
        _      <- evaluateAll(List("3", "4", "+"))
        result <- evaluateOne("*")
      } yield result

      program2.runA(Nil).value shouldBe 21
    }
  }

}
