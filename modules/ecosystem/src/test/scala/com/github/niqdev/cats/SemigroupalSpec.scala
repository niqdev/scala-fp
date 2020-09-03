package com.github.niqdev.cats

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class SemigroupalSpec extends AnyWordSpecLike with Matchers {

  "Semigroupal" should {

    "verify examples" in {
      import cats.Semigroupal
      // semigroupal

      Semigroupal[Option].product(Some(42), Some("hello")) shouldBe Some(42 -> "hello")
      Semigroupal[Option].product(None, Some("hello")) shouldBe None
      Semigroupal[Option].product(Some(42), None) shouldBe None

      Semigroupal.tuple3(Option(1), Option(2), Option(3)) shouldBe Some(Tuple3(1, 2, 3))
      Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int]) shouldBe None

      Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _) shouldBe Some(6)
      Semigroupal.map2(Option(1), Option.empty[Int])(_ + _) shouldBe None
    }

    "verify syntax" in {
      // tupled
      import cats.syntax.apply.catsSyntaxTuple3Semigroupal

      (Option(123), Option("abc"), Option(true)).tupled shouldBe Some(Tuple3(123, "abc", true))
    }

    /*
     * Internally mapN uses the Semigroupal to extract the values from the Option
     * and the Functor to apply the values to the function
     */
    "verify mapN" in {
      // functor + semigroupal
      // mapN
      import cats.syntax.apply.catsSyntaxTuple3Semigroupal

      case class Book(title: String, code: Int, pages: Int)

      val maybeBook: Option[Book] = Tuple3(
        Option("myTilte"),
        Option(123),
        Option(1000)
      ).mapN(Book.apply)

      maybeBook shouldBe Some(Book("myTilte", 123, 1000))
    }
  }

}
