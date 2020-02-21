package com.github.niqdev.cats

import cats.data.OptionT
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class OptionTSpec extends AnyWordSpecLike with Matchers {

  "OptionT" should {

    "verify examples" in {
      // monad: flatMap, map
      import cats.instances.list.catsStdInstancesForList
      // pure
      import cats.syntax.applicative.catsSyntaxApplicativeId

      type ListOption[A] = OptionT[List, A]

      val result = for {
        x <- OptionT(List(Option(10)))
        y <- 32.pure[ListOption]
      } yield x + y

      result.value shouldBe List(Some(42))
    }

    "verify Future" in {
      // monad: flatMap, map
      import java.util.concurrent.TimeUnit

      import cats.instances.future.catsStdInstancesForFuture

      import scala.concurrent.ExecutionContext.Implicits.global
      import scala.concurrent.duration.Duration
      import scala.concurrent.{ Await, Future }

      val futureOption: Future[Option[String]] = Future.successful(Some("hello"))

      val future: Future[String] = Future.successful("monad")

      val option: Option[String] = Some("transformer")

      val result: OptionT[Future, String] = for {
        fo <- OptionT(futureOption)
        f  <- OptionT.liftF(future)
        o  <- OptionT.fromOption[Future](option)
      } yield s"$fo $f $o"

      result.value.isInstanceOf[Future[Option[String]]] shouldBe true

      Await.result(result.value, Duration(1, TimeUnit.SECONDS)) match {
        case Some(value) =>
          value shouldBe "hello monad transformer"
        case None =>
          fail("unexpected failure")
      }
    }
  }

}
