package com.github.niqdev
package datastructure

sealed trait MyTree[+A]
final case class MyBranch[A](left: MyTree[A], right: MyTree[A]) extends MyTree[A]
final case class MyLeaf[A](value: A) extends MyTree[A]

object MyTree {

  object instances extends MyTreeInstances

  def branch[A](left: MyTree[A], right: MyTree[A]): MyTree[A] =
    MyBranch(left, right)

  def leaf[A](value: A): MyTree[A] =
    MyLeaf(value)
}

trait MyTreeInstances {

  implicit def myTreeFunctor[T]: Functor[MyTree] =
    new Functor[MyTree] {
      override def map[A, B](fa: MyTree[A])(f: A => B): MyTree[B] =
        fa match {
          case MyLeaf(a) =>
            MyLeaf(f(a))
          case MyBranch(left, right) =>
            MyBranch(myTreeFunctor.map(left)(f), myTreeFunctor.map(right)(f))
        }
    }
}
