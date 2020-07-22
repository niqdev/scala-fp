package com.github.niqdev.cats

import cats.Monoid
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class MonoidSpec extends AnyWordSpecLike with Matchers {

  "Monoid" should {

    "verify List" in {
      import cats.instances.int.catsKernelStdGroupForInt
      import cats.instances.option.catsKernelStdMonoidForOption
      import cats.instances.string.catsKernelStdMonoidForString
      import cats.syntax.semigroup.catsSyntaxSemigroup

      final case class Container(s: String, i: Int)

      implicit val containerMonoid: Monoid[Container] =
        Monoid.instance(
          Container("", 0),
          {
            case (Container(s0, i0), Container(s1, i1)) =>
              Container(s0 |+| s1, i0 |+| i1)
          }
        )

      def add[A: Monoid](items: List[A]): A =
        items.foldLeft(Monoid[A].empty)(Monoid[A].combine)

      add(List(Option(1), Option(2), Option(3))) shouldBe Some(6)
      add(List(Option(1), None, Option(3))) shouldBe Some(4)
      add(List(Container("a", 1), Container("b", 2), Container("c", 3))) shouldBe Container("abc", 6)
    }
  }

}
