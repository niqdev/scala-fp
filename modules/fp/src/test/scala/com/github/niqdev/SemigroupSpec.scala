package com.github.niqdev

import com.github.niqdev.Semigroup.instances.{intAdditionSemigroup, stringConcatenationSemigroup}
import org.scalatest.{Matchers, WordSpecLike}

final class SemigroupSpec extends WordSpecLike with Matchers {

  "Semigroup" should {

    "verify instances" in {
      List(1, 2, 3, 4).reduceLeft(intAdditionSemigroup.combine) shouldBe 10
      List("a", "b", "c").reduceRight(stringConcatenationSemigroup.combine) shouldBe "abc"
    }
  }
}
