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
  // TODO
}

trait FunctorSyntax {

  implicit final class FunctorOps[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit ev: Functor[F]): F[B] =
      ev.map(fa)(f)
  }
}
