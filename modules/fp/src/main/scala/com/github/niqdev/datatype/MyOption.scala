package com.github.niqdev
package datatype

sealed trait MyOption[+A]
final case class MySome[A](value: A) extends MyOption[A]
case object MyNone extends MyOption[Nothing]
