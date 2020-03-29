package com.github.niqdev

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class MonadSpec extends AnyWordSpecLike with Matchers {

  "Monad" should {

    "verify Id" in {
      import com.github.niqdev.Monad.instances.idMonad

      Monad[Id].pure(8) shouldBe identity[Int](8)
    }

    "verify MyList" in {
      import com.github.niqdev.Monad.instances.myListMonad

      Monad[MyList].pure(8) shouldBe MyList(8)

      def multiplyFour: Int => MyList[Int] = a => MyList(a * 4)
      def addOne: Int => MyList[Int]       = a => MyList(a + 1)
      def multiplyTwo: Int => MyList[Int]  = a => MyList(a * 2)

      val answer = Monad[MyList]
        .pure(5)
        .flatMap(multiplyFour)
        .flatMap(addOne)
        .flatMap(multiplyTwo)

      answer shouldBe MyCons(42, MyNil)
    }

    "verify MyTree" in {
      // TODO not implemented
    }

    "verify MyOption" in {
      import com.github.niqdev.Monad.instances.myOptionMonad

      val answer = for {
        a <- Monad[MyOption].pure(5)
        b <- Monad[MyOption].pure(4)
        c <- Monad[MyOption].pure(1)
        d <- Monad[MyOption].pure(2)
      } yield ((a * b) + c) * d

      answer shouldBe MySome(42)
    }

    /* FIXME no implicit found
    "verify MyEither" in {
      import com.github.niqdev.Monad.instances.myEitherMonad

      Monad[MyEither].pure(8)
    }
     */

    "verify Function1" in {
      // TODO not implemented
    }
  }

}
