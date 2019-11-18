package com.github.niqdev
package datastructure

sealed trait MyTree[+A]
final case class MyBranch[A](left: MyTree[A], right: MyTree[A]) extends MyTree[A]
final case class MyLeaf[A](value: A)                            extends MyTree[A]

object MyTree {

  def branch[A](left: MyTree[A], right: MyTree[A]): MyTree[A] =
    MyBranch(left, right)

  def leaf[A](value: A): MyTree[A] =
    MyLeaf(value)
}
