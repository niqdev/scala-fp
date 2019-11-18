package com.github.niqdev

import com.github.niqdev.Monoid.instances._
import org.scalatest.{ Matchers, WordSpecLike }

final class MonoidSpec extends WordSpecLike with Matchers {

  "Monoid" should {

    "verify instances" in {
      List(1, 2, 3, 4).foldLeft(intAdditionMonoid.empty)(intAdditionMonoid.combine) shouldBe 10
      List(1, 2, 3, 4).foldLeft(intMultiplicationMonoid.empty)(intMultiplicationMonoid.combine) shouldBe 24
      List("a", "b", "c").foldLeft(stringConcatenationMonoid.empty)(stringConcatenationMonoid.combine) shouldBe "abc"
      List(true, false).foldLeft(booleanAndMonoid.empty)(booleanAndMonoid.combine) shouldBe false
      List(true, false).foldLeft(booleanOrMonoid.empty)(booleanOrMonoid.combine) shouldBe true
      List(Set(1, 2, 3), Set(2, 3, 4))
        .foldLeft(setUnionMonoid[Int].empty)(setUnionMonoid[Int].combine) shouldBe Set(1, 2, 3, 4)

      val optionIntMonoid = optionMonoid(intAdditionMonoid)
      List(Some(1), Some(2), Some(3)).foldLeft(optionIntMonoid.empty)(optionIntMonoid.combine) shouldBe Some(
        6
      )
      List(Some(1), None, Some(3)).foldLeft(optionIntMonoid.empty)(optionIntMonoid.combine) shouldBe Some(4)
    }
  }
}
