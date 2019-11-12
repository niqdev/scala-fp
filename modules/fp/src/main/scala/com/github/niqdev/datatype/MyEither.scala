package com.github.niqdev
package datatype

final case class MyRight[+A](value: A) extends MyEither[Nothing, A]
final case class MyLeft[+E](error: E) extends MyEither[E, Nothing]

sealed trait MyEither[+E, +A] {

  def map[B](f: A => B): MyEither[E, B] =
    this match {
      case MyRight(value) => MyRight(f(value))
      case myLeft@MyLeft(_) => myLeft
    }

  // when mapping over the right side, promote the left type parameter
  // to some supertype, to satisfy the +E variance annotation
  def flatMap[EE >: E, B](f: A => MyEither[EE, B]): MyEither[EE, B] =
    this match {
      case MyRight(value) => f(value)
      case myLeft@MyLeft(_) => myLeft
    }
}

object MyEither {

  object instances extends MyEitherInstances
}

trait MyEitherInstances {

  // * is a kind-projector magic
  implicit def myEitherFunctor[E]: Functor[Either[E, *]] =
    new Functor[Either[E, *]] {
      override def map[A, B](fa: Either[E, A])(f: A => B): Either[E, B] =
        fa.map(f)
    }
}
