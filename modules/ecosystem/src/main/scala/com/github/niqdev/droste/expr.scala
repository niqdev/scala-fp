package com.github.niqdev.droste

import cats.Functor
import higherkindness.droste.data.Fix
import higherkindness.droste.{ Algebra, scheme }

// https://github.com/precog/matryoshka/blob/master/tests/shared/src/test/scala/matryoshka/example/expr.scala
// https://github.com/precog/matryoshka/blob/master/tests/shared/src/test/scala/matryoshka/example/mathExpr.scala
// https://github.com/higherkindness/droste/blob/master/athema/src/main/scala/higherkindness/athema/expr.scala
object expr {

  // data structure
  sealed trait Expr[V, T]
  final case class Constant[V, T](value: V)   extends Expr[V, T]
  final case class Negate[V, T](x: T)         extends Expr[V, T]
  final case class Add[V, T](x: T, y: T)      extends Expr[V, T]
  final case class Subtract[V, T](x: T, y: T) extends Expr[V, T]
  final case class Multiply[V, T](x: T, y: T) extends Expr[V, T]
  final case class Divide[V, T](x: T, y: T)   extends Expr[V, T]
  object Expr {
    // fixed points
    type Fixed[V] = Fix[Expr[V, *]]
    def constant[V](value: V): Fixed[V]                 = Fix(Constant(value))
    def negate[V](x: Fixed[V]): Fixed[V]                = Fix(Negate(x))
    def add[V](x: Fixed[V], y: Fixed[V]): Fixed[V]      = Fix(Add(x, y))
    def subtract[V](x: Fixed[V], y: Fixed[V]): Fixed[V] = Fix(Subtract(x, y))
    def multiply[V](x: Fixed[V], y: Fixed[V]): Fixed[V] = Fix(Multiply(x, y))
    def divide[V](x: Fixed[V], y: Fixed[V]): Fixed[V]   = Fix(Divide(x, y))

    // functor
    implicit def exprFunctor[V]: Functor[Expr[V, *]] =
      new Functor[Expr[V, *]] {
        override def map[A, B](fa: Expr[V, A])(f: A => B): Expr[V, B] =
          fa match {
            case Constant(value) => Constant(value)
            case Negate(x)       => Negate(f(x))
            case Add(x, y)       => Add(f(x), f(y))
            case Subtract(x, y)  => Subtract(f(x), f(y))
            case Multiply(x, y)  => Multiply(f(x), f(y))
            case Divide(x, y)    => Divide(f(x), f(y))
          }
      }
  }

  private[this] def evalIntExpr[V]: Algebra[Expr[V, *], Int] =
    Algebra {
      // non-variable type argument Int in type pattern com.github.niqdev.droste.expr.Constant[Int,_]
      // is unchecked since it is eliminated by erasure
      case c: Constant[Int @unchecked, _] => c.value
      case Negate(x)                      => -x
      case Add(x, y)                      => x + y
      case Subtract(x, y)                 => x - y
      case Multiply(x, y)                 => x * y
      case Divide(x, y)                   => x / y
    }

  // cata F[A] => A (requires functor)
  def evalInt: Expr.Fixed[Int] => Int = scheme.cata(evalIntExpr[Int])
}
