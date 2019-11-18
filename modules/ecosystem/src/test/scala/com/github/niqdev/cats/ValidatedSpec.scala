package com.github.niqdev.cats

import org.scalatest.{ Matchers, WordSpecLike }

final class ValidatedSpec extends WordSpecLike with Matchers {

  "Validated" should {

    /* FIXME missing Semigroupal[Validated]
    "verify examples" in {
      import cats.data._
      import cats.data.Validated._
      import cats.implicits._
      import cats.Semigroupal
      import cats.instances.list._
      import cats.instances.string._

      Semigroupal[Validated[List[String], Int]].product(
        Validated.Valid(123),
        Validated.invalid(List("Error 1")),
        Validated.Valid(456),
        Validated.invalid(List("Error 2"))
      )
    }
     */

    "verify examples" in {
      import cats.data.Validated

      Validated.Valid(123).isValid shouldBe true
      Validated.Invalid(List("error")).isInvalid shouldBe true

      Validated.valid[List[String], Int](123).isValid shouldBe true
      Validated.invalid[List[String], Int](List("error")).isInvalid shouldBe true
    }

    "verify syntax (1)" in {
      import cats.syntax.validated.catsSyntaxValidatedId

      123.valid[List[String]].isValid shouldBe true
      List("error").invalid[Int].isInvalid shouldBe true
    }

    /* FIXME missing import Applicative[Validated]
    "verify syntax (2)" in {
      import cats.data.Validated
      // pure
      import cats.syntax.applicative.catsSyntaxApplicativeId
      // raiseError
      import cats.syntax.applicativeError.catsSyntaxApplicativeErrorId

      123.pure[Validated[List[String], Int]]
      List("Badness").raiseError[Validated[List[String], Int], Int]
    }
     */

    "verify helper" in {
      import cats.data.Validated

      Validated.catchOnly[NumberFormatException]("invalid".toInt).isInvalid shouldBe true
      Validated.catchNonFatal(sys.error("error")).isInvalid shouldBe true
      Validated.fromTry(scala.util.Try("invalid".toInt)).isInvalid shouldBe true
      Validated.fromEither[String, Int](Left("error")).isInvalid shouldBe true
      Validated.fromOption[String, Int](None, "error").isInvalid shouldBe true
    }

    "verify Form Validation" in {
      import cats.data.Validated
      // catchOnly
      import cats.syntax.either.catsSyntaxEitherObject
      // leftMap
      import cats.syntax.either.catsSyntaxEither
      // semigroupal
      import cats.instances.list.catsKernelStdMonoidForList
      // mapN
      import cats.syntax.apply.catsSyntaxTuple2Semigroupal

      case class User(name: String, age: Int)

      type FormData    = Map[String, String]
      type FailFast[A] = Either[List[String], A]
      type FailSlow[A] = Validated[List[String], A]

      def getValue(name: String)(data: FormData): FailFast[String] =
        data.get(name).toRight(List(s"$name field not specified"))

      val getName = getValue("name") _
      getName(Map("name" -> "myName")) shouldBe Right("myName")
      getName(Map.empty) shouldBe Left(List("name field not specified"))

      def parseInt(name: String)(data: String): FailFast[Int] =
        Either
          .catchOnly[NumberFormatException](data.toInt)
          .leftMap(_ => List(s"$name must be an integer"))

      parseInt("age")("42") shouldBe Right(42)
      parseInt("age")("invalid") shouldBe Left(List("age must be an integer"))

      def nonBlank(name: String)(data: String): FailFast[String] =
        Right(data).ensure(List(s"$name cannot be blank"))(_.nonEmpty)

      nonBlank("name")("myName") shouldBe Right("myName")
      nonBlank("name")("") shouldBe Left(List("name cannot be blank"))

      def nonNegative(name: String)(data: Int): FailFast[Int] =
        Right(data).ensure(List(s"$name must be non-negative"))(_ >= 0)

      nonNegative("age")(42) shouldBe Right(42)
      nonNegative("age")(-1) shouldBe Left(List("age must be non-negative"))

      def readName(data: FormData): FailFast[String] =
        getValue("name")(data)
          .flatMap(nonBlank("name"))

      def readAge(data: FormData): FailFast[Int] =
        getValue("age")(data)
          .flatMap(nonBlank("age"))
          .flatMap(parseInt("age"))
          .flatMap(nonNegative("age"))

      def readUser(data: FormData): FailSlow[User] =
        (readName(data).toValidated, readAge(data).toValidated)
          .mapN(User.apply)

      readUser(Map("name" -> "myName", "age" -> "42")) shouldBe Validated.valid(User("myName", 42))
      readUser(Map("age" -> "-1")) shouldBe Validated.invalid(
        List("name field not specified", "age must be non-negative")
      )
    }
  }

}
