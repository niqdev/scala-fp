package com.github.niqdev
package laws

import com.github.niqdev.datastructure.{MyBranch, MyList, MyTree}
import com.github.niqdev.datatype.{MyNone, MyOption, MySome}
import org.scalacheck.{Arbitrary, Gen}

object ArbitraryImplicits {

  implicit def arbMyList[T](implicit a: Arbitrary[T]): Arbitrary[MyList[T]] =
    Arbitrary {
      Gen.listOf(a.arbitrary).map(MyList.apply)
    }

  implicit def arbMyTree[T](implicit a: Arbitrary[T]): Arbitrary[MyTree[T]] =
    Arbitrary {

      val genMyLeaf: Gen[MyTree[T]] =
        for (value <- Arbitrary.arbitrary[T]) yield MyTree.leaf(value)

      def genMyBranch(size: Int): Gen[MyBranch[T]] = for {
        n <- Gen.choose(size / 3, size / 2)
        left <- genMyTree(n)
        right <- genMyTree(n)
      } yield MyBranch(left, right)

      def genMyTree(size: Int): Gen[MyTree[T]] =
        if (size <= 0) genMyLeaf
        else Gen.frequency((1, genMyLeaf), (3, genMyBranch(size)))

      Gen.sized(genMyTree)
    }

  implicit def arbMyOption[T](implicit a: Arbitrary[T]): Arbitrary[MyOption[T]] =
    Arbitrary(Gen.option(a.arbitrary).map {
      case Some(value) => MySome(value)
      case None => MyNone
    })
}
