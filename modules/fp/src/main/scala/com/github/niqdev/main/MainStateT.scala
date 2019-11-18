package com.github.niqdev
package main

import com.github.niqdev.IO.instances.ioMonad

final case class IntState(value: Int)

object MainStateT {

  type StateIO[A] = StateT[IO, IntState, A]

  def liftIO[A](io: IO[A]): StateIO[A] =
    StateT[IO, IntState, A] { s: IntState =>
      io.map(a => (s, a))
    }

  def updateState(f: IntState => Int): StateIO[Int] =
    StateT[IO, IntState, Int] { oldState: IntState =>
      val newValue = f(oldState)
      // lenses !!!
      val newState = oldState.copy(value = newValue)
      IO(newState -> newValue)
    }

  val answer: StateIO[Int] =
    for {
      _       <- liftIO(IO(println("hello")))
      state20 <- updateState(oldState => oldState.value * 4)
      _       <- updateState(oldState => oldState.value + 1)
      _       <- updateState(oldState => oldState.value * 2)
      _       <- liftIO(IO(println("magic number")))
    } yield state20

  def main(args: Array[String]): Unit = {
    val result = answer.run(IntState(5)).unsafeRun

    // hello
    // magic number
    // StateT[F, S, A] = [S=IntState(42)][A=20]
    println(s"StateT[F, S, A] = [S=${result._1}][A=${result._2}]")
  }

}
