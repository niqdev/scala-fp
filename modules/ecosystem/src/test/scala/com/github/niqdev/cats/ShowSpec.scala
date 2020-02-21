package com.github.niqdev.cats

import java.util.Date

import cats.Show
import cats.syntax.show.toShow
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class ShowSpec extends AnyWordSpecLike with Matchers {

  "Show" should {

    "verify apply" in {
      import cats.instances.int.catsStdShowForInt
      import cats.instances.string.catsStdShowForString

      Show.apply[Int].show(123) shouldBe "123"
      Show[String].show("abc") shouldBe "abc"
    }

    "verify Cat" in {
      import cats.instances.int.catsStdShowForInt
      import cats.instances.string.catsStdShowForString

      final case class Cat(name: String, age: Int, color: String)

      implicit val catShow: Show[Cat] = Show.show { cat =>
        val name  = cat.name.show
        val age   = cat.age.show
        val color = cat.color.show
        s"$name is a $age year-old $color cat."
      }

      val cat      = Cat("Garfield", 8, "ginger and black")
      val expected = "Garfield is a 8 year-old ginger and black cat."

      Show[Cat](catShow).show(cat) shouldBe expected
      cat.show shouldBe expected
    }

    "verify Date" in {
      implicit val dateShow: Show[Date] =
        Show.show(date => s"${date.getTime} ms since the epoch")

      val date     = new Date(123)
      val expected = "123 ms since the epoch"

      Show.apply[Date](dateShow).show(date) shouldBe expected
      date.show shouldBe expected
    }
  }

}
