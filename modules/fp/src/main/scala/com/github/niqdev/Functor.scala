package com.github.niqdev

// F stands for effect
// ev stands for evidence
// F[_]: type constructors and higher-kinded types
// import scala.language.higherKinds

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object Functor {

  object instances extends FunctorInstances
  object syntax extends FunctorSyntax

  def apply[F[_]](implicit ev: Functor[F]): Functor[F] = ev
}

trait FunctorInstances {

  // see catsStdMonadForFunction1
  implicit def function1Functor[T]: Functor[T => *] =
    new Functor[Function[T, *]] {
      override def map[A, B](fa: Function[T, A])(f: A => B): Function[T, B] =
        fa andThen f
    }
}

trait FunctorSyntax {

  implicit final class FunctorOps[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit ev: Functor[F]): F[B] =
      ev.map(fa)(f)
  }
}
