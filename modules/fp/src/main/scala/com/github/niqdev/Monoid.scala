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
}

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
