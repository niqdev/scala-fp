package com.github.niqdev
package datatype

sealed trait MyEither[+E, +A]
final case class MyRight[+A](value: A) extends MyEither[Nothing, A]
final case class MyLeft[+E](error: E) extends MyEither[E, Nothing]
