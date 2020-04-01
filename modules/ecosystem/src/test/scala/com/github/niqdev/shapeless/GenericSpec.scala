package com.github.niqdev.shapeless

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import shapeless.{ Generic, HNil, Inl, Inr }

final class GenericSpec extends AnyWordSpecLike with Matchers {

  // PRODUCT / AND / CASE CLASS => HList / :: / HNil
  case class Dog(name: String, years: Int, friendly: Boolean)
  case class Book(title: String, yearOfPublication: Int, published: Boolean)

  // COPRODUCT / SUM / OR / SEALED TRAIT => :+: / Inl / Inr / CNil
  sealed trait Shape
  case class Rectangle(width: Double, height: Double) extends Shape
  case class Circle(radius: Double)                   extends Shape

  "Generic" should {

    "verify product" in {
      val dogGeneric  = Generic[Dog]
      val bookGeneric = Generic[Book]

      dogGeneric.to(Dog("tommy", 8, true)) shouldBe "tommy" :: 8 :: true :: HNil
      bookGeneric.to(Book("isbn", 1900, true)) shouldBe "isbn" :: 1900 :: true :: HNil

      dogGeneric.from(bookGeneric.to(Book("aaa", 888, false))) shouldBe Dog("aaa", 888, false)
    }

    "verify coproduct" in {
      val shapeGeneric = Generic[Shape]

      shapeGeneric.to(Rectangle(3.0, 4.0)) shouldBe Inr(Inl(Rectangle(3.0, 4.0)))
      shapeGeneric.to(Circle(2.0)) shouldBe Inl(Circle(2.0))
    }
  }
}
