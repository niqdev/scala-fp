package com.github.niqdev
package datastructure

sealed trait MyList[+A]
final case class MyCons[+A](head: A, tail: MyList[A]) extends MyList[A]
case object MyNil extends MyList[Nothing]
