package com.github.niqdev
package datatype

final case class MySome[A](value: A) extends MyOption[A]
case object MyNone extends MyOption[Nothing]

// TODO
sealed trait MyOption[+A] {

  def map[B](f: A => B): MyOption[B] =
    this match {
      case MyNone => MyNone
      case MySome(a) => MySome(f(a))
    }

  def flatMap[B](f: A => MyOption[B]): MyOption[B] =
    this match {
      case MyNone => MyNone
      case MySome(a) => f(a)
    }

  def getOrElse[B >: A](default: => B): B = ???

  def orElse[B >: A](ob: => MyOption[B]): MyOption[B] = ???

  def filter(f: A => Boolean): MyOption[A] = ???

}
