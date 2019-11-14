package com.github.niqdev.cats

import org.scalatest.{Matchers, WordSpecLike}

final class MonadErrorSpec extends WordSpecLike with Matchers {

  "MonadError" should {

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

    "verify examples" in {
      import cats.instances.try_.catsStdInstancesForTry
      import cats.syntax.applicativeError.catsSyntaxApplicativeErrorId

      val exception: Throwable = new RuntimeException("error")
      exception.raiseError[scala.util.Try, Int] shouldBe scala.util.Failure(exception)
    }
  }

}
