package com.github.niqdev
package datastructure

import scala.annotation.tailrec

final case class MyCons[+A](head: A, tail: MyList[A]) extends MyList[A]
case object MyNil extends MyList[Nothing]

sealed trait MyList[+A] {

  // foldRight start from right (xLast op acc) ==> (A, B)
  def foldRightUnsafe[B](z: B)(f: (A, B) => B): B =
    this match {
      case MyNil => z
      case MyCons(head, tail) =>
        f(head, tail.foldRightUnsafe(z)(f))
    }

  // foldLeft start from left (acc op xFirst) ==> (B, A)
  // result is reversed
  def foldLeftUnsafe[B](z: B)(f: (B, A) => B): B =
    this match {
      case MyNil => z
      case MyCons(head, tail) =>
        tail.foldLeftUnsafe(f(z, head))(f)
    }

  // stack safe
  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    @tailrec
    def loop(current: MyList[A], result: B): B =
      current match {
        case MyNil => result
        case MyCons(head, tail) =>
          loop(tail, f(result, head))
      }
    loop(this, z)
  }

  def length: Int =
    foldLeft(0)((acc, _) => acc + 1)

  def reverse: MyList[A] =
    foldLeft(MyNil: MyList[A])((accumulator, current) => MyCons(current, accumulator))

  // stack safe
  def foldRight[B](z: B)(f: (A, B) => B): B =
    reverse.foldLeft(z)((b, a) => f(a, b))

  def map[B](f: A => B): MyList[B] =
    foldRight(MyNil: MyList[B])((current, accumulator) => MyCons(f(current), accumulator))
}

object MyList {

  def apply[A](as: A*): MyList[A] =
    if (as.isEmpty) MyNil
    else MyCons(as.head, apply(as.tail: _*))
}

trait MyListInstances {

  implicit val myListFunctor: Functor[MyList] =
    new Functor[MyList] {
      override def map[A, B](fa: MyList[A])(f: A => B): MyList[B] =
        fa.map(f)
    }
}
