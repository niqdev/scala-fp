package com.github.niqdev.scalacheck

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class ModelSpec extends AnyWordSpecLike with Matchers {

  "Model" should {

    "generate Person" in {
      Generators.genPerson.sample.collect {
        case person => println(person)
      }
    }

    "generate Person as Json" in {
      Generators.genJson[models.Person](Generators.genPerson).sample.collect {
        case personJson => println(personJson)
      }
    }
  }
}
