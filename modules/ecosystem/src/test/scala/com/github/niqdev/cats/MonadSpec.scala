package com.github.niqdev.cats

import cats.Monad
import org.scalatest.{Matchers, WordSpecLike}

final class MonadSpec extends WordSpecLike with Matchers {

  "Monad" should {

    "verify option" in {
      import cats.instances.option.catsStdInstancesForOption

      Monad[Option].pure(3) shouldBe Some(3)
      Monad[Option].flatMap(Option(3))(value => Some(value * 3)) shouldBe Some(9)
      Monad[Option].map(Option(3))(_ * 3) shouldBe Some(9)
    }

    "verify sumSquare" in {
      import cats.instances.option.catsStdInstancesForOption
      import cats.syntax.applicative.catsSyntaxApplicativeId
      import cats.syntax.flatMap.toFlatMapOps
      import cats.syntax.functor.toFunctorOps

      def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
        a.flatMap(a0 => b.map(b0 => a0 * a0 + b0 * b0))

      // context bound
      def sumSquareFor[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
        for {
          a0 <- a // flatMap
          b0 <- b // map
        } yield a0 * a0 + b0 * b0

      val expected = Some(13)
      sumSquare(Option(2), Option(3)) shouldBe expected
      sumSquareFor[Option](2.pure[Option], 3.pure[Option]) shouldBe expected
    }

    "verify Id" in {
      import cats.Id
      import cats.syntax.applicative.catsSyntaxApplicativeId
      import cats.syntax.flatMap.toFlatMapOps
      import cats.syntax.functor.toFunctorOps

      Monad[Id].pure(42) shouldBe 42
      Monad[Id].flatMap(42)(_ / 2) shouldBe 21

      val sum = for {
        a <- 2.pure[Id]
        b <- 3.pure[Id]
        c <- 4.pure[Id]
      } yield a + b + c

      sum shouldBe 9
    }

    // fail-fast error handling
    "verify Either" in {
      import cats.syntax.either.{catsSyntaxEither, catsSyntaxEitherId, catsSyntaxEitherObject}

      3.asRight[String] shouldBe Right(3)
      "error".asLeft[Int] shouldBe Left("error")

      Either.catchOnly[NumberFormatException]("error".toInt)
        .leftMap(_.getMessage) shouldBe Left("For input string: \"error\"")
      Either.catchNonFatal(sys.error("error"))
        .leftMap(_.getMessage) shouldBe Left("error")
      Either.fromTry(scala.util.Try("error".toInt))
        .leftMap(_.getMessage) shouldBe Left("For input string: \"error\"")
      Either.fromOption[String, Int](None, "error") shouldBe Left("error")

      "error".asLeft[Int].leftMap(_.reverse) shouldBe Left("rorre")
      6.asRight[String].bimap(_.reverse, _ * 7) shouldBe Right(42)
      "error".asLeft[Int].bimap(_.reverse, _ * 7) shouldBe Left("rorre")
    }

    "verify MonadError" in {
      import cats.instances.try_.catsStdInstancesForTry
      import cats.syntax.applicativeError.catsSyntaxApplicativeErrorId

      val exception: Throwable = new RuntimeException("error")
      exception.raiseError[scala.util.Try, Int] shouldBe scala.util.Failure(exception)
    }
  }

}
