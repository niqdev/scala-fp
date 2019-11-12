package com.github.niqdev.datastructure

import org.scalatest.{Matchers, WordSpecLike}

final class MyListSpec extends WordSpecLike with Matchers {

  private[this] val myList = MyList(1, 2, 3, 4)

  "MyList" should {

    "verify foldRightUnsafe" in {
      myList.foldRightUnsafe(0)(_ + _) shouldBe 10
    }

    "verify foldRight" in {
      myList.foldRight(0)(_ + _) shouldBe 10
    }

    "verify foldLeftUnsafe" in {
      myList.foldLeftUnsafe(0)(_ + _) shouldBe 10
    }

    "verify foldLeft" in {
      myList.foldLeft(0)(_ + _) shouldBe 10
    }

    "verify length" in {
      myList.length shouldBe 4
    }

    "verify reverse" in {
      myList.reverse shouldBe MyList(4, 3, 2, 1)
    }

    "verify map" in {
      myList.map(_ * 3) shouldBe MyList(3, 6, 9, 12)
    }
  }

}
