package com.github.niqdev
package datatype

final case class MySome[A](value: A) extends MyOption[A]
case object MyNone                   extends MyOption[Nothing]

sealed trait MyOption[+A] {

  def map[B](f: A => B): MyOption[B] =
    this match {
      case MyNone    => MyNone
      case MySome(a) => MySome(f(a))
    }

  def flatMap[B](f: A => MyOption[B]): MyOption[B] =
    this match {
      case MyNone    => MyNone
      case MySome(a) => f(a)
    }
}

object MyOption {
  def apply[A](a: A): MyOption[A] =
    if (a == null) MyNone else MySome(a)
}
