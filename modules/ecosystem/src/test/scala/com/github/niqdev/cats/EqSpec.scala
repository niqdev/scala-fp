package com.github.niqdev.cats

import cats.Eq
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final case class Cat(name: String, age: Int, color: String)

object CatInstances {
  import cats.instances.int.catsKernelStdOrderForInt
  import cats.instances.string.catsKernelStdOrderForString
  import cats.syntax.eq.catsSyntaxEq

  implicit val catEq: Eq[Cat] =
    Eq.instance[Cat] { (cat1, cat2) =>
      cat1.name === cat2.name &&
      cat1.age === cat2.age &&
      cat1.color === cat2.color
    }
}

// define outside: conflicts with org.scalactic.TripleEqualsSupport
object EqHelper {
  import cats.syntax.eq.catsSyntaxEq

  def verifyEqvSyntax[T](x: T, y: T)(implicit ev: Eq[T]): Boolean =
    x === y

  def verifyNeqvSyntax[T](x: T, y: T)(implicit ev: Eq[T]): Boolean =
    x =!= y
}

final class EqSpec extends AnyWordSpecLike with Matchers {

  "Eq" should {

    "verify examples" in {
      import cats.instances.int.catsKernelStdOrderForInt

      Eq[Int].eqv(123, 123) shouldBe true
      Eq[Int].neqv(123, 456) shouldBe true
    }

    "verify instances" in {
      import CatInstances.catEq
      import cats.instances.option.catsKernelStdEqForOption

      val cat1 = Cat("Garfield", 35, "orange and black")
      val cat2 = Cat("Heathcliff", 30, "orange and black")

      EqHelper.verifyEqvSyntax(cat1, cat2) shouldBe false
      EqHelper.verifyNeqvSyntax(cat1, cat2) shouldBe true
      Eq[Cat].eqv(cat1, cat2) shouldBe false
      Eq[Cat].neqv(cat1, cat2) shouldBe true

      EqHelper.verifyEqvSyntax(Option(cat1), Option.empty[Cat]) shouldBe false
      EqHelper.verifyNeqvSyntax(Option(cat1), Option(cat2)) shouldBe true
    }
  }

}
