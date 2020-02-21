package com.github.niqdev.cats

import cats.Id
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class IdSpec extends AnyWordSpecLike with Matchers {

  "Id" should {

    "verify examples" in {
      import cats.Monad

      Monad[Id].pure(42) shouldBe 42
      Monad[Id].flatMap(42)(_ / 2) shouldBe 21
    }

    "verify composition" in {
      import cats.syntax.applicative.catsSyntaxApplicativeId
      import cats.syntax.flatMap.toFlatMapOps
      import cats.syntax.functor.toFunctorOps

      val sum = for {
        a <- 2.pure[Id]
        b <- 3.pure[Id]
        c <- 4.pure[Id]
      } yield a + b + c

      sum shouldBe 9
    }
  }

}
