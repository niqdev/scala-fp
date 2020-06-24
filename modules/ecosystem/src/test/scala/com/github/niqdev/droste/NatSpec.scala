package com.github.niqdev.droste

import com.github.niqdev.droste.nat._
import higherkindness.droste.data.Fix
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class NatSpec extends AnyWordSpecLike with Matchers {

  // natural number 2
  private[this] val nat2: Fix[Nat] = Fix(Succ(Fix(Succ(Fix[Nat](Zero)))))

  "Nat" should {

    "create a Nat from an Int" in {
      intToNat(2) should be(nat2)
    }

    "create an Int from a Nat" in {
      natToInt(nat2) shouldBe 2
    }

    "convert an Int to Nat and back to Int" in {
      intToNatToInt(2) shouldBe natToInt(intToNat(2))
    }
  }
}
