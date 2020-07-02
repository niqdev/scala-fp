package com.github.niqdev.scalacheck

import magnolia._
import mercator._
import org.scalacheck.Gen

import scala.language.experimental.macros

// alternative to Arbitrary
trait Random[T] {
  def random: Gen[T]
}

trait RandomLowPriority {

  def instance[T](f: =>Gen[T]): Random[T] =
    new Random[T] {
      override def random: Gen[T] = f
    }

  implicit val randomString: Random[String] =
    instance[String](Gen.alphaNumStr)

  implicit val randomInt: Random[Int] =
    instance[Int](Gen.chooseNum(Int.MinValue, Int.MaxValue))

  implicit val randomBoolean: Random[Boolean] =
    instance[Boolean](Gen.oneOf(true, false))
}

object Random extends RandomLowPriority {
  def apply[T](implicit ev: Random[T]): Random[T] = ev

  type Typeclass[T] = Random[T]

  def combine[T](ctx: CaseClass[Random, T]): Random[T] =
    new Random[T] {
      override def random: Gen[T] =
        ctx.constructMonadic { param => param.typeclass.random }
    }

  implicit val genMonadic: Monadic[Gen] = new Monadic[Gen] {
    override def point[A](value: A): Gen[A] =
      Gen.const(value)

    override def flatMap[A, B](from: Gen[A])(fn: A => Gen[B]): Gen[B] =
      from.flatMap(fn)

    override def map[A, B](from: Gen[A])(fn: A => B): Gen[B] =
      from.map(fn)
  }

  implicit def gen[T]: Random[T] = macro Magnolia.gen[T]
}

case class RandomExample(myString: String, myInt: Int, myBoolean: Boolean)

object RandomApp extends App {
  import io.circe._
  import io.circe.generic.semiauto._
  import io.circe.syntax._

  @scala.annotation.nowarn
  implicit val randomExampleEncoder: Encoder[RandomExample] = deriveEncoder[RandomExample]

  println(Random[String].random.sample)
  println(Random.gen[RandomExample].random.sample)
  println(implicitly[Random[RandomExample]].random.map(_.asJson).sample)
}
