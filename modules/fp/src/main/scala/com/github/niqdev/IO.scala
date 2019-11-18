package com.github.niqdev

// https://github.com/jdegoes/lambdaconf-2014-introgame#effectful-monads
// https://github.com/alvinj/IOMonad/blob/master/src/main/scala/io_monad/IO.scala
sealed abstract class IO[A] private (thunk: =>A) {

  def unsafeRun: A = thunk

  def flatMap[B](f: A => IO[B]): IO[B] = {
    val iob: IO[B] = f(unsafeRun)
    val b: B       = iob.unsafeRun
    IO(b)
  }

  def map[B](f: A => B): IO[B] =
    flatMap(a => IO(f(a)))
}

object IO {

  object instances extends IOInstances

  // lifts any by-name parameter into the IO context
  def apply[A](a: =>A): IO[A] = new IO[A](a) {}

  def unit: IO[Unit] = IO(())
}

trait IOInstances {

  implicit val ioMonad: Monad[IO] =
    new Monad[IO] {
      override def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] =
        fa.flatMap(f)

      override def pure[A](a: A): IO[A] =
        IO(a)
    }
}
