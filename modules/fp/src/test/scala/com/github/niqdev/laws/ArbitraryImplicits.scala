package com.github.niqdev
package laws

import com.github.niqdev.datastructure.{MyBranch, MyList, MyTree}
import com.github.niqdev.datatype.{MyNone, MyOption, MySome}
import org.scalacheck.{Arbitrary, Gen}

object ArbitraryImplicits extends CommonArbitraryImplicits with WrapperArbitraryImplicits

trait CommonArbitraryImplicits {

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

  implicit def arbMyEither[T, U](implicit arbT: Arbitrary[T], arbU: Arbitrary[U]): Arbitrary[MyEither[T, U]] =
    Arbitrary(Gen.oneOf(arbT.arbitrary.map(MyLeft(_)), arbU.arbitrary.map(MyRight(_))))
}

// effect/context/wrapper
protected[laws] final case class Wrapper[A](value: A)

trait WrapperArbitraryImplicits {

  // Arbitrary[F[A]]
  implicit def arbWrapperFA[A](implicit arbA: Arbitrary[A]): Arbitrary[Wrapper[A]] =
    Arbitrary {
      arbA.arbitrary.map(a => Wrapper(a))
    }

  // Arbitrary[A => F[B]]
  implicit def arbWrapperAFB[A, B](implicit arbB: Arbitrary[B]): Arbitrary[A => Wrapper[B]] =
    Arbitrary {
      arbB.arbitrary.map(b => (_: A) => Wrapper(b))
    }

  // Arbitrary[F[A] => B]
  implicit def arbWrapperFAB[A, B](implicit arbB: Arbitrary[B]): Arbitrary[Wrapper[A] => B] =
    Arbitrary {
      arbB.arbitrary.map(b => (_: Wrapper[A]) => b)
    }

  // Arbitrary[F[A] => F[B]]
  implicit def arbWrapperFAFB[A, B](implicit arbFB: Arbitrary[Wrapper[B]]): Arbitrary[Wrapper[A] => Wrapper[B]] =
    Arbitrary {
      arbFB.arbitrary.map(fb => (_: Wrapper[A]) => fb)
    }
}
