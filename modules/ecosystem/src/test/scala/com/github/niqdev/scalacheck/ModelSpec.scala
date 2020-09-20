package com.github.niqdev.scalacheck

import com.github.niqdev.model.models._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class ModelSpec extends AnyWordSpecLike with Matchers {

  "Model" should {

    "generate Person" in {
      Generators.genPerson.sample.collect { case person =>
        println(person)
      }
    }

    "generate Person as Json" in {
      Generators.genJson[Person](Generators.genPerson).sample.collect { case personJson =>
        println(personJson)
      }
    }
  }
}
