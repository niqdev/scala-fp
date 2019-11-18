package com.github.niqdev

import java.time.{ ZoneId, ZonedDateTime }

import org.scalatest.{ Matchers, WordSpecLike }

final class ShowSpec extends WordSpecLike with Matchers {

  case class Person(name: String, age: Int)

  object Person {
    implicit val personShow: Show[Person] =
      (value: Person) => {
        val name = Show[String].show(value.name)
        val age  = Show[Int].show(value.age)
        s"name=$name|age=$age"
      }
  }

  "Show" should {

    "verify instances" in {
      Show[String].show("abc") shouldBe "abc"
      Show[Int].show(1) shouldBe "1"

      val date = ZonedDateTime.of(2019, 11, 10, 0, 0, 0, 0, ZoneId.of("Europe/Dublin"))
      Show[ZonedDateTime].show(date) shouldBe "2019-11-10Z"
    }

    "verify option instance" in {
      Show[Option[Person]].show(Some(Person("name", 8))) shouldBe "name=name|age=8"
      Show[Option[Person]].show(Option.empty[Person]) shouldBe "NONE"
      Show[Option[String]].show(Option.empty[String]) shouldBe "NONE"
    }

    "verify syntax" in {
      import com.github.niqdev.show.ShowOps

      Person("name", 8).show shouldBe "name=name|age=8"
    }

    "verify implicitly" in {
      implicitly[Show[Person]].show(Person("name", 8)) shouldBe "name=name|age=8"
    }
  }

}
