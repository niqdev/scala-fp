package com.github.niqdev

/**
  * Semigroup Type Class
  */
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

  implicit def listSemigroup[A]: Semigroup[List[A]] =
    Semigroup.instance(_ ::: _)

  implicit def myListSemigroup[A]: Semigroup[MyList[A]] =
    Semigroup.instance(_ appendList _)
}
