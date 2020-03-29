package com.github.niqdev.fs2

import cats.effect.IO
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class StreamSpec extends AnyWordSpecLike with Matchers {

  "Stream" should {

    "verify pure streams" in {
      import cats.instances.int.catsKernelStdGroupForInt

      // F: effect type
      // O: output type
      // Stream[F, O]
      fs2.Stream[fs2.Pure, Int](1, 2, 3).toList shouldBe List(1, 2, 3)
      fs2.Stream[fs2.Pure, Int](1, 2, 3).foldMonoid.toList shouldBe List(6)
    }

    "verify effectful streams" in {

      val intIO: IO[Int] = IO {
        println("hello")
        42
      }
      val stream = fs2.Stream.eval[IO, Int](intIO)

      val vectorIntIO: IO[Vector[Int]] = stream.compile.toVector
      vectorIntIO.unsafeRunSync shouldBe Vector(42)

      // discards any output values
      val unitIO: IO[Unit] = stream.compile.drain
      unitIO.unsafeRunSync shouldBe (_: Unit)
    }

    "verify streams composition" in {
      val pure = for {
        a <- fs2.Stream(1)
        b <- fs2.Stream(2)
        c <- fs2.Stream(3)
      } yield a + b + c

      pure.compile.toList shouldBe List(6)

      val effectful = for {
        a <- fs2.Stream.eval(IO(1))
        b <- fs2.Stream.eval(IO(2))
        c <- fs2.Stream.eval(IO(3))
      } yield a + b + c

      effectful.compile.toList.unsafeRunSync shouldBe List(6)

      val effectfulError = for {
        a <- fs2.Stream.eval(IO(1))
        b <- fs2.Stream.raiseError[IO](new Exception("error"))
        c <- fs2.Stream.eval(IO(3))
      } yield a + b + c

      scala.util.Try(effectfulError.compile.toList.unsafeRunSync) match {
        case scala.util.Failure(error) =>
          error.getMessage shouldBe "error"
        case scala.util.Success(value) =>
          fail(s"unexpected result: $value")
      }
    }
  }

}
