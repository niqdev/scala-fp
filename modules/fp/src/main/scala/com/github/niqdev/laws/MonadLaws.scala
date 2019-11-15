package com.github.niqdev
package laws

trait MonadLaws[F[_]] {

  implicit def F: Monad[F]

  // calling pure and transforming the result with f is the same as calling f
  // pure(a).flatMap(f) == func(a)
  def leftIdentityLaw[A, B](a: A, f: A => F[B]): Boolean =
    F.flatMap(F.pure(a))(f) == f(a)

  // passing pure to flatMap is the same as doing nothing
  // m.flatMap(pure) == m
  def rightIdentityLaw[A](fa: F[A]): Boolean =
    F.flatMap(fa)(F.pure) == fa

  // flatMapping over two functions f and g
  // is the same as flatMapping over f and then flatMapping over g
  // m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
  def associativityLaw[A, B, C](fa: F[A], f: A => F[B], g: B => F[C]): Boolean =
    F.flatMap(F.flatMap(fa)(f))(g) == F.flatMap(fa)(a => F.flatMap(f(a))(g))
}

object MonadLaws {

  def apply[F[_] : Monad]: MonadLaws[F] =
    new MonadLaws[F] {
      val F: Monad[F] = implicitly[Monad[F]]
    }
}
