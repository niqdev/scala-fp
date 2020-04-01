package com.github.niqdev.shapeless

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import shapeless.{ HNil, LabelledGeneric }

final class LabelledGenericSpec extends AnyWordSpecLike with Matchers {

  case class House(street: String, rooms: Int, available: Boolean)

  "LabelledGeneric" should {

    "verify" in {
      val house = House("myStreet", 4, true)
      val gen   = LabelledGeneric[House]

      gen.to(house) shouldBe "myStreet" :: 4 :: true :: HNil
//      val fieldType: FieldType[String with labelled.KeyTag[Symbol with tag.Tagged[ {
//        type street
//      }], String], String] = gen.to(house).head
    }
  }
}
