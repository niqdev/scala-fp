package com.github.niqdev

import com.github.niqdev.internal.{IO, StateT}

final case class IntState(i: Int)

object MainStateT extends App {

  def toState(f: IntState => Int): StateT[IO, IntState, Int] = StateT[IO, IntState, Int] {
    oldState: IntState =>
      val newValue = f(oldState)
      val newState = oldState.copy(i = newValue)

      //IO((newState, newValue))
      //IO(newState -> newValue)
      IO(newState, newValue)
  }

  val addAndMultiply: StateT[IO, IntState, Int] = for {
    _ <- toState(oldState => 1 + oldState.i)
    _ <- toState(oldState => 2 * oldState.i)
    _ <- toState(oldState => 8 + oldState.i)
    lastState <- toState(oldState => 3 * oldState.i)
  } yield lastState

  val initialState = IntState(3)
  val result: IO[(IntState, Int)] = addAndMultiply.run(initialState)
  val program: IO[Unit] = result.map(tuple => println(s"IntState: ${tuple._1}"))

  // IntState: IntState(48)
  program.run

}
