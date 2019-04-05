package com.github.niqdev
package internal

// https://github.com/alvinj/FPMonadTransformers/blob/master/src/main/scala/monads/Monad.scala
trait Monad[M[_]] {

  // point
  def lift[A](a: A): M[A]

  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

  def map[A, B](ma: M[A])(f: A => B): M[B] =
    flatMap(ma)(a => lift(a))
}

object Monad {

  implicit val optionMonad = new Monad[Option] {
    override def lift[A](a: A): Option[A] =
      Some(a)

    override def flatMap[A, B](ma: Option[A])(f: A => Option[B]): Option[B] =
      ma.flatMap(f)
  }

  implicit val ioMonad = new Monad[IO] {
    override def lift[A](a: A): IO[A] =
      IO(a)

    override def flatMap[A, B](ma: IO[A])(f: A => IO[B]): IO[B] =
      ma.flatMap(f)
  }
}
