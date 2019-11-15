package com.github.niqdev

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Show Type Class
 */
trait Show[T] {
  def show(value: T): String
}

// type class instances
sealed trait ShowInstances {

  implicit val stringShow: Show[String] =
    (value: String) => value

  implicit val intShow: Show[Int] =
    (value: Int) => value.toString

  implicit val zonedDateTimeShow: Show[ZonedDateTime] =
    Show.showInstance(date => s"${date.format(DateTimeFormatter.ISO_DATE)}")

  implicit def optionShow[T](implicit s: Show[T]): Show[Option[T]] = {
    case Some(value) =>
      // intellij complains: required T found Any
      s.show(value)
    case None =>
      "NONE"
  }
}

// interface object: defines interface methods
object Show extends ShowInstances {

  def show[T](value: T)(implicit s: Show[T]): String =
    s.show(value)

  // instance helper
  def showInstance[T](f: T => String): Show[T] = new Show[T] {
    override def show(value: T): String = f(value)
  }

  // helper
  def apply[T](implicit S: Show[T]): Show[T] = S
}

// interface syntax: defines extension methods
trait ShowSyntax {

  implicit class ShowOps[T](value: T) {
    def show(implicit s: Show[T]): String =
      s.show(value)
  }
}
