package com.github.niqdev.cats
import cats.syntax.foldable.catsSyntaxFoldOps
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class FoldableSpec extends AnyWordSpecLike with Matchers {

  "Foldable" should {

    "verify collectFirstSomeM" in {
      List.empty[Option[Int]].collectFirstSomeM(List(_)) shouldBe List(None)
      List[Option[Int]](None, None).collectFirstSomeM(List(_)) shouldBe List(None)
      List[Option[Int]](Some(1)).collectFirstSomeM(List(_)) shouldBe List(Some(1))
      List(None, None, Some(1), Some(2), None).collectFirstSomeM(List(_)) shouldBe List(Some(1))
    }
  }
}
