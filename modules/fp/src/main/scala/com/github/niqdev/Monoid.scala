package com.github.niqdev

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {

  object instances extends MonoidInstances

  def apply[A](implicit ev: Monoid[A]): Monoid[A] = ev

  def instance[A](zero: A)(f: (A, A) => A): Monoid[A] =
    new Monoid[A] {
      override def combine(x: A, y: A): A = f(x, y)
      override def empty: A = zero
    }

  def instanceS[A](zero: A)(implicit ev: Semigroup[A]): Monoid[A] =
    instance(zero)(ev.combine)
}

trait MonoidInstances {

  implicit val intAdditionMonoid: Monoid[Int] =
    Monoid.instanceS(0)(Semigroup.instances.intAdditionSemigroup)

  implicit val intMultiplicationMonoid: Monoid[Int] =
    Monoid.instance(1)(_ * _)

  implicit val stringConcatenationMonoid: Monoid[String] =
    Monoid.instanceS("")(Semigroup.instances.stringConcatenationSemigroup)

  implicit val booleanAndMonoid: Monoid[Boolean] =
    Monoid.instance(true)(_ && _)

  implicit val booleanOrMonoid: Monoid[Boolean] =
    Monoid.instance(false)(_ || _)

  implicit def setUnionMonoid[A]: Monoid[Set[A]] =
    Monoid.instance(Set.empty[A])(_ union _)

  implicit def optionMonoid[A](implicit m: Monoid[A]): Monoid[Option[A]] =
    new Monoid[Option[A]] {
      override def combine(x: Option[A], y: Option[A]): Option[A] =
        (x, y) match {
          case (None, None) =>
            None
          case (maybeX, None) =>
            maybeX
          case (None, maybeY) =>
            maybeY
          case (Some(maybeX), Some(maybeY)) =>
            Some(m.combine(maybeX, maybeY))
        }
      override def empty: Option[A] = None
    }
}
