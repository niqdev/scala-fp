package com.github.niqdev.droste

import cats.Functor
import higherkindness.droste.data.Fix
import higherkindness.droste.{ Algebra, Coalgebra, scheme }

// https://github.com/precog/matryoshka/blob/master/tests/shared/src/test/scala/matryoshka/example/nat.scala
object nat {

  sealed trait Nat[+A]
  final case object Zero                extends Nat[Nothing]
  final case class Succ[A](previous: A) extends Nat[A]

  implicit val natFunctor: Functor[Nat] = new Functor[Nat] {
    override def map[A, B](fa: Nat[A])(f: A => B): Nat[B] =
      fa match {
        case Zero           => Zero
        case Succ(previous) => Succ(f(previous))
      }
  }

  val toNat: Coalgebra[Nat, Int] = Coalgebra {
    case 0        => Zero
    case previous => Succ(previous - 1)
  }

  val toInt: Algebra[Nat, Int] = Algebra {
    case Zero           => 0
    case Succ(previous) => previous + 1
  }

  val intToNat: Int => Fix[Nat] = scheme.ana(toNat)
  val natToInt: Fix[Nat] => Int = scheme.cata(toInt)
  val intToNatToInt: Int => Int = scheme.hylo(toInt, toNat)
}
