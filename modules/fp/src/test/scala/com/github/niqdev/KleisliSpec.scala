package com.github.niqdev

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class KleisliSpec extends AnyWordSpecLike with Matchers {

  "Kleisli" should {

    "verify standard function composition" in {

      val multiply: Int => Int =
        x => x * 2

      val toString: Int => String =
        x => s"value is $x"

      val multiplyAndThenToString: Int => String =
        multiply andThen toString

      val multiplyComposeToString: Int => String =
        toString compose multiply

      multiplyAndThenToString(3) shouldBe "value is 6"
      multiplyComposeToString(3) shouldBe "value is 6"
    }

    "verify composition" in {

      val parse: Kleisli[MyOption, String, Int] =
        Kleisli((s: String) => if (s.matches("-?[0-9]+")) MySome(s.toInt) else MyNone)

      val multiply: Kleisli[MyOption, Int, Double] =
        Kleisli((i: Int) => MySome(i * 2))

      val toString: Kleisli[MyOption, Double, String] =
        Kleisli((d: Double) => MySome(s"value is: $d"))

      import Monad.instances.myOptionMonad

      val parseAndThenMultiplyAndThenToString: Kleisli[MyOption, String, String] =
        parse.andThen(multiply).andThen(toString)

      val toStringComposeMultiplyComposeParse: Kleisli[MyOption, String, String] =
        toString.compose(multiply).compose(parse)

      parseAndThenMultiplyAndThenToString.run("123") shouldBe MySome("value is: 246.0")
      parseAndThenMultiplyAndThenToString.run("aaa") shouldBe MyNone
      toStringComposeMultiplyComposeParse.run("123") shouldBe MySome("value is: 246.0")
      toStringComposeMultiplyComposeParse.run("aaa") shouldBe MyNone
    }
  }

}
