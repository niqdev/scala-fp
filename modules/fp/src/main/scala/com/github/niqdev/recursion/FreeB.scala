package com.github.niqdev.recursion

// Free Boolean algebra for all A
// https://arosien.github.io/talks/free-boolean-algebras.html
// https://www.youtube.com/watch?v=6-afaw_ht80
// https://engineering.wingify.com/posts/Free-objects
sealed trait FreeB[A]

object FreeB {
  final implicit def freeBSyntax[A](fa: FreeB[A]): FreeBOps[A] =
    new FreeBOps[A](fa)

  // boolean operator
  case class Pure[A](a: A)                           extends FreeB[A]
  case class True[A]()                               extends FreeB[A]
  case class False[A]()                              extends FreeB[A]
  case class And[A](left: FreeB[A], right: FreeB[A]) extends FreeB[A]
  case class Or[A](left: FreeB[A], right: FreeB[A])  extends FreeB[A]
  case class Not[A](fa: FreeB[A])                    extends FreeB[A]
}

final class FreeBOps[A](private val self: FreeB[A]) extends AnyVal {

  def &&(that: FreeB[A]): FreeB[A] = FreeB.And(self, that)
  def ||(that: FreeB[A]): FreeB[A] = FreeB.Or(self, that)
  def unary_! : FreeB[A]           = FreeB.Not(self)

  // augment the boolean algebra with a domain-specific evaluator
  def run(f: A => Boolean): Boolean =
    self match {
      case FreeB.Pure(a)          => f(a) // separate boolean algebra from domain specific-logic
      case FreeB.True()           => true
      case FreeB.False()          => false
      case FreeB.And(left, right) => left.run(f) && right.run(f)
      case FreeB.Or(left, right)  => left.run(f) || right.run(f)
      case FreeB.Not(fa)          => !fa.run(f)
    }

  def pretty(f: A => String): String =
    self match {
      case FreeB.Pure(a)          => f(a)
      case FreeB.True()           => "true"
      case FreeB.False()          => "false"
      case FreeB.And(left, right) => s"(${left.pretty(f)} && ${right.pretty(f)})"
      case FreeB.Or(left, right)  => s"(${left.pretty(f)} || ${right.pretty(f)})"
      case FreeB.Not(fa)          => s"!${fa.pretty(f)}"
    }
}

sealed trait Predicate

object Predicate {
  // algebraic data type
  case class AtLeast13(i: Int)       extends Predicate
  case class NonEmptyName(s: String) extends Predicate

  // interpreter
  val eval: Predicate => Boolean = {
    case Predicate.AtLeast13(i)    => i >= 13
    case Predicate.NonEmptyName(s) => s.nonEmpty
  }

  val pretty: Predicate => String = {
    case Predicate.AtLeast13(i)    => s"($i >= 13)"
    case Predicate.NonEmptyName(s) => s"""('$s' != '')"""
  }
}

object Main extends App {

  val predicates: FreeB[Predicate] =
    FreeB.Pure[Predicate](Predicate.AtLeast13(13)) &&
      FreeB.Pure[Predicate](Predicate.NonEmptyName("aaa"))

  println(s"${predicates.run(Predicate.eval)}")
  println(s"${predicates.pretty(Predicate.pretty)}")
}
