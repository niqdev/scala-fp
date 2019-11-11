package com.github.niqdev

import org.scalatest.{Matchers, WordSpecLike}

final class SemigroupSpec extends WordSpecLike with Matchers {

  "Semigroup" should {

    "verify instances" in {
      import Semigroup.instances.{intAdditionSemigroup, stringConcatenationSemigroup}

      List(1, 2, 3, 4).reduceLeft(intAdditionSemigroup.combine) shouldBe 10
      List("a", "b", "c").reduceRight(stringConcatenationSemigroup.combine) shouldBe "abc"
    }
  }
}
