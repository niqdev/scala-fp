package com.github.niqdev
package laws

trait MonoidLaws[A] extends SemigroupLaws[A] {

  implicit def F: Monoid[A]

  // 0 + a == a
  def monoidLeftIdentity(a: A): Boolean =
    F.combine(F.empty, a) == a

  // a + 0 == a
  def monoidRightIdentity(a: A): Boolean =
    F.combine(a, F.empty) == a
}

object MonoidLaws {

  def apply[A: Monoid]: MonoidLaws[A] =
    new MonoidLaws[A] {
      val F: Monoid[A] = implicitly[Monoid[A]]
    }
}
