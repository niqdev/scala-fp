package com.github.niqdev
package datastructure

import org.scalatest.{ Matchers, WordSpecLike }

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

    "verify append" in {
      myList.append(5) shouldBe MyList(1, 2, 3, 4, 5)
    }

    "verify appendList" in {
      myList.appendList(MyList(5, 6)) shouldBe MyList(1, 2, 3, 4, 5, 6)
    }

    "verify flatMap" in {
      def multiplyFour: Int => MyList[Int] = a => MyList(a * 4)
      def addOne: Int => MyList[Int]       = a => MyList(a + 1)
      def multiplyTwo: Int => MyList[Int]  = a => MyList(a * 2)

      val answer = MyList(5)
        .flatMap(multiplyFour)
        .flatMap(addOne)
        .flatMap(multiplyTwo)

      answer shouldBe MyCons(42, MyNil)
    }

    "verify filter" in {
      // even
      myList.filter(_ % 2 == 0) shouldBe MyList(2, 4)
    }
  }

}
