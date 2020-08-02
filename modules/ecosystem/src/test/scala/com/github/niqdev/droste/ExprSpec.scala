package com.github.niqdev.droste

import com.github.niqdev.droste.expr.Expr._
import com.github.niqdev.droste.expr._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

// sbt "test:testOnly *ExprSpec"
final class ExprSpec extends AnyWordSpecLike with Matchers {

  "Expr" should {

    "evaluate Int (1)" in {
      evalInt(negate[Int](constant(1))) shouldBe -1
    }

    "evaluate Int (2)" in {
      val expr: Fixed[Int] =
        multiply[Int](
          add[Int](constant(1), constant(1)),
          subtract[Int](constant(2), constant(5))
        )
      evalInt(expr) shouldBe -6
    }
  }
}
