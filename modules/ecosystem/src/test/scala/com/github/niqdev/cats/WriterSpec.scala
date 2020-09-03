package com.github.niqdev.cats

import cats.data.Writer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class WriterSpec extends AnyWordSpecLike with Matchers {

  "Writer" should {

    "verify examples" in {
      val log    = Vector("msg1", "msg2", "msg3")
      val result = 123

      val writer = Writer(log, result)
      writer.written shouldBe log
      writer.value shouldBe result
      writer.run shouldBe Tuple2(log, result)
    }

    "verify composition" in {
      import cats.syntax.applicative.catsSyntaxApplicativeId
      import cats.syntax.writer.catsSyntaxWriterId

      /*
       * The log in a Writer is preserved when we map or flatMap over it.
       * flatMap appends the logs from the source Writer and the result of the user’s sequencing function.
       * For this reason it's good practice to use a log type that has an
       * efficient append and concatenate operations, such as a Vector
       */
      type Logged[A] = Writer[Vector[String], A]

      // requires semigroup
      val writerFor = for {
        a <- 2.pure[Logged] // only result, no log
        _ <- Vector("add-2").tell // append only log, no result
        b <- 3.pure[Logged]
        _ <- Vector("add-3").tell
        c <- 4.writer(Vector("add-4")) // result and log
        _ <- Vector("end").tell
      } yield a + b + c // result

      writerFor.written shouldBe Vector("add-2", "add-3", "add-4", "end")
      writerFor.value shouldBe 9
      writerFor.run shouldBe Tuple2(Vector("add-2", "add-3", "add-4", "end"), 9)
    }
  }

}
