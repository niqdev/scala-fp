package com.github.niqdev

// F stands for effect
// ev stands for evidence

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

object Semigroup {

  object instances extends SemigroupInstances

  // syntax helper
  def apply[A](implicit ev: Semigroup[A]): Semigroup[A] = ev

  // instance helper
  def instance[A](f: (A, A) => A): Semigroup[A] =
    (x: A, y: A) => f(x, y)
}

trait SemigroupInstances {

  implicit val intAdditionSemigroup: Semigroup[Int] =
    Semigroup.instance(_ + _)

  implicit val stringConcatenationSemigroup: Semigroup[String] =
    Semigroup.instance(_ + _)
}

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
