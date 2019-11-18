package com.github.niqdev

import com.github.niqdev.Semigroup.instances.{
  intAdditionSemigroup,
  listSemigroup,
  myListSemigroup,
  stringConcatenationSemigroup
}
import org.scalatest.{ Matchers, WordSpecLike }

final class SemigroupSpec extends WordSpecLike with Matchers {

  "Semigroup" should {

    "verify instances" in {
      List(1, 2, 3, 4).reduceLeft(intAdditionSemigroup.combine) shouldBe 10
      List("a", "b", "c").reduceRight(stringConcatenationSemigroup.combine) shouldBe "abc"

      List(List("error1", "error2"), List(1, 2), List(true, false))
        .reduceRight(listSemigroup.combine) shouldBe List("error1", "error2", 1, 2, true, false)

      val expected = MyList("error1", "error2", "message1", "message2", "foo1", "foo2")
      MyList(MyList("error1", "error2"), MyList("message1", "message2"), MyList("foo1", "foo2"))
        .foldRight(MyNil: MyList[String])(myListSemigroup.combine) shouldBe expected
    }
  }
}
