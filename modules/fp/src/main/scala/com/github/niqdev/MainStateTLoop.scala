package com.github.niqdev

import com.github.niqdev.internal.{IO, MyConsole, Show, StateT}

import scala.util.{Failure, Success, Try}

final case class SumState(i: Int)

object MainStateTLoop extends App {

  // monadic lifting
  def liftIoIntoStateT[S, A](io: IO[A]): StateT[IO, S, A] =
    StateT[IO, S, A] { oldState: S =>
      // IO[(S, A)]
      io.map(a => (oldState, a))
    }

  def printlnAsStateT[S, T: Show](value: T): StateT[IO, S, Unit] =
    liftIoIntoStateT[S, Unit](MyConsole[IO].println(value))

  def readLineAsStateT[S]: StateT[IO, S, String] =
    liftIoIntoStateT[S, String](MyConsole[IO].readLine())

  // TODO Option[Int]
  // 0 is identity for sum
  def fromUnsafeInt(value: String): Int =
    Try(value.toInt) match {
      case Success(i) => i
      case Failure(_) => 0
    }

  def sumAsStateT(i: Int): StateT[IO, SumState, Int] = StateT[IO, SumState, Int] {
    oldState: SumState =>
      val newValue = i + oldState.i
      val newState = oldState.copy(i = newValue)
      IO(newState, newValue)
  }

  def sumLoop: StateT[IO, SumState, Unit] =
    for {
      _ <- printlnAsStateT[SumState, String]("Type a number, or [q] to quit: ")
      input <- readLineAsStateT[SumState]
      _ <- if (input == "q") {
        liftIoIntoStateT[SumState, Unit](IO.unit)
      } else for {
        i <- liftIoIntoStateT[SumState, Int](IO(fromUnsafeInt(input)))
        _ <- sumAsStateT(i)
        _ <- sumLoop
      } yield ()
    } yield ()

  val initialState = SumState(0)

  sumLoop
    .run(initialState)
    // FIXME remove println
    .map(tuple => println(s"result: ${tuple._1}"))
    .run

}
