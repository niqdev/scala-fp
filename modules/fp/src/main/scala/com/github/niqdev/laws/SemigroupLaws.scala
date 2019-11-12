package com.github.niqdev
package laws

trait SemigroupLaws[A] {

  implicit def F: Semigroup[A]

  // (x + y) + z == x + (y + z)
  def semigroupAssociativity(x: A, y: A, z: A): Boolean =
    F.combine(F.combine(x, y), z) == F.combine(x, F.combine(y, z))
}

object SemigroupLaws {

  // syntax helper
  def apply[A](implicit ev: Semigroup[A]): SemigroupLaws[A] =
    new SemigroupLaws[A] {
      val F: Semigroup[A] = ev
    }
}
