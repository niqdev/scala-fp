package com.github.niqdev.cats

import org.scalatest.{Matchers, WordSpecLike}

final class IdSpec extends WordSpecLike with Matchers {

  "Monad" should {

    "verify Id" in {
      import cats.{Id, Monad}
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
  }

}
