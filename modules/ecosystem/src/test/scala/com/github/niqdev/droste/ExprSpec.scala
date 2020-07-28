package com.github.niqdev.droste

import com.github.niqdev.droste.expr._
import higherkindness.droste.data.Fix
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

// sbt "test:testOnly *ExprSpec"
final class ExprSpec extends AnyWordSpecLike with Matchers {

  "Expr" should {

    "evaluate Int" in {
      // FIXME
      val expr: Fix[Expr] = Fix(Constant(1))
      evalInt(expr) shouldBe "TODO"
    }
  }
}
