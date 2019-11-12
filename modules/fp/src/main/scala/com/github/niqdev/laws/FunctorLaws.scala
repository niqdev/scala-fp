package com.github.niqdev
package laws

trait FunctorLaws[F[_]] {

  implicit def F: Functor[F]

  // calling map with the identity function is the same as doing nothing
  // fa.map(a => a) == fa
  def identityLaw[A](fa: F[A])(implicit f: Functor[F]): Boolean =
    f.map(fa)(identity[A]) == fa

  // mapping with two functions f and g is the same as mapping with f and then mapping with g
  // fa.map(g(f(_))) == fa.map(f).map(g)
  def compositionLaw[A, B, C](fa: F[A], f: A => B, g: B => C): Boolean =
    F.map(F.map(fa)(f))(g) == F.map(fa)(f andThen g)
}

object FunctorLaws {

  def apply[F[_]](implicit ev: Functor[F]): FunctorLaws[F] =
    new FunctorLaws[F] {
      val F: Functor[F] = ev
    }
}
