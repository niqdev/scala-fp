package com.github.niqdev.free

// Free Boolean algebra for all A
// Free objects are a way to build generalized interpreter patterns
// https://arosien.github.io/talks/free-boolean-algebras.html
// https://www.youtube.com/watch?v=6-afaw_ht80
// https://engineering.wingify.com/posts/Free-objects
// https://github.com/stucchio/Oldmonk/tree/master/src/main/scala/com/vwo/oldmonk/free
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

  // alternative: `def run[B: Interpreter](f: A => B): B` see typelevel.org/algebra
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

  def normalize: FreeB[A] =
    self match {
      case FreeB.And(FreeB.True(), right) => right.normalize
      case FreeB.And(left, FreeB.True())  => left.normalize
      case FreeB.And(FreeB.False(), _)    => FreeB.False()
      case FreeB.And(_, FreeB.False())    => FreeB.False()
      case FreeB.Or(FreeB.True(), _)      => FreeB.True()
      case FreeB.Or(_, FreeB.True())      => FreeB.True()
      case FreeB.Or(FreeB.False(), right) => right.normalize
      case FreeB.Or(left, FreeB.False())  => left.normalize
      case _                              => self
    }

  /* it doesn't compile ???
  // add these methods
  object FreeB {
    def pure[A](a: A): FreeB[A] = FreeB.Pure(a)
    def `true`[A]: FreeB[A] = FreeB.True()
    def `false`[A]: FreeB[A] = FreeB.False()
  }
  def runPartial(pf: PartialFunction[A, Boolean]): FreeB[A] =
    self.run { (a: A) =>
      pf.lift(a).fold(FreeB.pure(a)) { boolean =>
        if (boolean) FreeB.`true` else FreeB.`false`
      }
    }
   */

  // evaluate FreeB[A] expressions to: Validated or other data structures
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

  println(s"${(FreeB.True() && FreeB.False()).normalize}")
  println(s"${(FreeB.True() && FreeB.True()).normalize}")
}
