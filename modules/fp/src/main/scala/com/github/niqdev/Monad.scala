package com.github.niqdev

trait Monad[F[_]] extends Applicative[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => pure(f(a)))
}

object Monad {

  def apply[F[_]](implicit ev: Monad[F]): Monad[F] = ev
}
