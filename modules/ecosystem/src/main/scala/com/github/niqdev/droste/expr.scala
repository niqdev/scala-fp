package com.github.niqdev.droste

import cats.Functor
import higherkindness.droste.data.Fix
import higherkindness.droste.{ Algebra, scheme }

// https://github.com/precog/matryoshka/blob/master/tests/shared/src/test/scala/matryoshka/example/expr.scala
// https://github.com/precog/matryoshka/blob/master/tests/shared/src/test/scala/matryoshka/example/mathExpr.scala
// https://github.com/higherkindness/droste/blob/master/athema/src/main/scala/higherkindness/athema/expr.scala
object expr {

  sealed trait Expr[A]
  final case class Constant[A](value: A)   extends Expr[A]
  final case class Negate[A](x: A)         extends Expr[A]
  final case class Add[A](x: A, y: A)      extends Expr[A]
  final case class Subtract[A](x: A, y: A) extends Expr[A]
  final case class Multiply[A](x: A, y: A) extends Expr[A]
  final case class Divide[A](x: A, y: A)   extends Expr[A]
  object Expr {
    implicit val exprFunctor: Functor[Expr] = new Functor[Expr] {
      override def map[A, B](fa: Expr[A])(f: A => B): Expr[B] =
        fa match {
          // TODO
          // abstract type B in type pattern com.github.niqdev.droste.expr.Constant[B] is unchecked since it is eliminated by erasure
          //case c: Constant[B @unchecked] => c: Constant[B]
          case Constant(value) => Constant(f(value))
          case Negate(x)       => Negate(f(x))
          case Add(x, y)       => Add(f(x), f(y))
          case Subtract(x, y)  => Subtract(f(x), f(y))
          case Multiply(x, y)  => Multiply(f(x), f(y))
          case Divide(x, y)    => Divide(f(x), f(y))
        }
    }
  }

  private[this] val evalIntExpr: Algebra[Expr, Int] = Algebra {
    case Constant(value) => value
    case Negate(x)       => -x
    case Add(x, y)       => x + y
    case Subtract(x, y)  => x - y
    case Multiply(x, y)  => x * y
    case Divide(x, y)    => x / y
  }

  val evalInt: Fix[Expr] => Int = scheme.cata(evalIntExpr)
}
