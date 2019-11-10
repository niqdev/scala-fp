package com.github.niqdev.old.internal

trait Show[T] {
  def show(t: T): String
}

object Show {

  implicit val stringShow: Show[String] =
    (t: String) => t

  implicit val intShow: Show[Int] =
    (t: Int) => s"$t"
}
