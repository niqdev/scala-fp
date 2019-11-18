package com.github.niqdev.cats

import cats.Monad
import org.scalatest.{ Matchers, WordSpecLike }

final class MonadSpec extends WordSpecLike with Matchers {

  "Monad" should {

    "verify Option" in {
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

      def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
        a.flatMap(a0 => b.map(b0 => a0 * a0 + b0 * b0))

      // context bound
      def sumSquareFor[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
        for {
          a0 <- a // flatMap
          b0 <- b // flatMap
        } yield a0 * a0 + b0 * b0 // map

      val expected = Some(13)
      sumSquare(Option(2), Option(3)) shouldBe expected
      sumSquareFor[Option](2.pure[Option], 3.pure[Option]) shouldBe expected
    }
  }

}
