package com.github.niqdev.droste

import com.github.niqdev.droste.nat._
import org.scalacheck.Gen
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

final class NatProp extends AnyWordSpecLike with ScalaCheckPropertyChecks with Matchers {

  // Int.MaxValue throws StackOverflowError
  "Nat" should {
    "verify hylo" in {
      forAll(Gen.chooseNum(0, 1000)) { n => intToNatToInt(n) shouldBe natToInt(intToNat(n)) }
    }
  }
}
