package com.github.niqdev.recursion

// Free Boolean algebra for all A
sealed trait FreeB[A]

object FreeB {
  final implicit def freeBSyntax[A](fa: FreeB[A]): FreeBOps[A] =
    new FreeBOps[A](fa)

  // boolean operator
  case class Pure[A](a: A) extends FreeB[A]
  case class True[A]() extends FreeB[A]
  case class False[A]() extends FreeB[A]
  case class And[A](left: FreeB[A], right: FreeB[A]) extends FreeB[A]
  case class Or[A](left: FreeB[A], right: FreeB[A]) extends FreeB[A]
  case class Not[A](fa: FreeB[A]) extends FreeB[A]
}

final class FreeBOps[A](private val self: FreeB[A]) extends AnyVal {

  def &&(that: FreeB[A]): FreeB[A] = FreeB.And(self, that)
  def ||(that: FreeB[A]): FreeB[A] = FreeB.Or(self, that)
  def unary_!(): FreeB[A] = FreeB.Not(self)

  // augment the boolean algebra with a domain-specific evaluator
  def run(f: A => Boolean): Boolean =
    self match {
      case FreeB.Pure(a) => f(a) // separate boolean algebra from domain specific-logic
      case FreeB.True() => true
      case FreeB.False() => false
      case FreeB.And(left, right) => left.run(f) && right.run(f)
      case FreeB.Or(left, right) => left.run(f) || right.run(f)
      case FreeB.Not(fa) => !fa.run(f)
    }
}

sealed trait Predicate

object Predicate {
  // algebraic data type
  case class AtLeast13(i: Int) extends Predicate
  case class NonEmptyName(s: String) extends Predicate

  // interpreter
  val eval: Predicate => Boolean = {
    case Predicate.AtLeast13(i) => i >= 13
    case Predicate.NonEmptyName(s) => s.nonEmpty
  }
}

object Main extends App {

  val predicates: FreeB[Predicate] =
    FreeB.Pure[Predicate](Predicate.AtLeast13(13)) &&
      FreeB.Pure[Predicate](Predicate.NonEmptyName("aaa"))

  println(s"${predicates.run(Predicate.eval)}")
}
