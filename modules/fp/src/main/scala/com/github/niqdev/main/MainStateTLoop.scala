package com.github.niqdev
package main

final case class StateContainer[A](value: A)

object MainStateTLoop {

  def putStrLnAsStateT[F[_] : Functor : Console, S, T: Show](value: T): StateT[F, S, Unit] =
    StateT.liftF[F, S, Unit](Console[F].putStrLn(value))

  def getStrLnAsStateT[F[_] : Functor : Console, S]: StateT[F, S, String] =
    StateT.liftF[F, S, String](Console[F].getStrLn)

  def fromUnsafe[A: Monoid](value: String)(f: String => A): A =
    scala.util.Try(f(value)).toOption.getOrElse(Monoid[A].empty)

  def combineStateT[F[_] : Applicative, A: Semigroup](value: A): StateT[F, StateContainer[A], A] =
    StateT[F, StateContainer[A], A] { oldState: StateContainer[A] =>
      val newValue = Semigroup[A].combine(value, oldState.value)
      val newState = oldState.copy(value = newValue)
      Applicative[F].pure(newState -> newValue)
    }

  def combineLoop[F[_] : Monad : Console, A: Monoid](f: String => A): StateT[F, StateContainer[A], Unit] =
    for {
      _ <- putStrLnAsStateT[F, StateContainer[A], String]("Type a number, or [q] to quit: ")
      input <- getStrLnAsStateT[F, StateContainer[A]]
      _ <- if (input == "q") {
        StateT.liftF[F, StateContainer[A], Unit](Applicative[F].pure(Monoid[A].empty))
      } else for {
        i <- StateT.liftF[F, StateContainer[A], A](Applicative[F].pure(fromUnsafe(input)(f)))
        _ <- combineStateT[F, A](i)
        _ <- combineLoop[F, A](f)
      } yield ()
    } yield ()

  def program: StateContainer[Int] = {
    import com.github.niqdev.Console.instances.ioConsole
    import com.github.niqdev.IO.instances.ioMonad
    import com.github.niqdev.Monoid.instances.intAdditionMonoid

    combineLoop[IO, Int](_.toInt).run(StateContainer(0)).unsafeRun._1
  }

  def main(args: Array[String]): Unit =
    println(s"StateContainer[Int] = ${program.value}")
}
