package com.github.niqdev.droste

import com.github.niqdev.droste.expr._
import com.github.niqdev.droste.expr.Expr._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

// sbt "test:testOnly *ExprSpec"
final class ExprSpec extends AnyWordSpecLike with Matchers {

  "Expr" should {

    "evaluate Int" in {
      val expr: Fixed[Int] = negate[Int](constant(1))
      evalInt(expr) shouldBe -1
    }
  }
}
