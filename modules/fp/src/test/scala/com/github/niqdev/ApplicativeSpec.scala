package com.github.niqdev

import org.scalatest.{Matchers, WordSpecLike}

final class ApplicativeSpec extends WordSpecLike with Matchers {

  "Applicative" should {

    "verify Id" in {
      import com.github.niqdev.Applicative.instances.idApplicative

      Applicative[Id].pure(8) shouldBe identity[Int](8)
    }

    "verify MyList" in {
      import com.github.niqdev.Applicative.instances.myListApplicative

      Applicative[MyList].pure(42) shouldBe MyList(42)

      val myConsF: MyList[Int => Long] = MyList.cons(_.toLong + 1L)
      val myConsInt: MyList[Int] = MyList.cons(7)

      Applicative[MyList].ap(myConsF)(myConsInt) shouldBe MyList.cons(8)

      Applicative[MyList].product(MyList.cons(8), MyList.cons(42)) shouldBe MyList.cons((8, 42))
      Applicative[MyList].product(MyList.cons(8), MyList.empty) shouldBe MyList.empty
      Applicative[MyList].product(MyList.empty, MyList.cons(42)) shouldBe MyList.empty
      Applicative[MyList].product(MyList.empty, MyList.empty) shouldBe MyList.empty
    }

    "verify MyTree" in {
      // TODO not implemented
    }

    "verify MyOption" in {
      import com.github.niqdev.Applicative.instances.myOptionApplicative

      // applicative
      Applicative[MyOption].pure(8) shouldBe MySome(8)
      Applicative[MyOption].pure(null) shouldBe MyNone

      val mySomeF: MyOption[Int => Long] = MySome(_.toLong + 1L)
      val myNoneF: MyOption[Int => Long] = MyNone
      val mySomeInt: MyOption[Int] = MySome(3)
      val myNoneInt: MyOption[Int] = MyNone

      // apply
      Applicative[MyOption].ap(mySomeF)(mySomeInt) shouldBe MySome(4)
      Applicative[MyOption].ap(myNoneF)(mySomeInt) shouldBe MyNone
      Applicative[MyOption].ap(mySomeF)(myNoneInt) shouldBe MyNone
      Applicative[MyOption].ap(myNoneF)(myNoneInt) shouldBe MyNone

      // semigroupal
      Applicative[MyOption].product(MySome(8), MySome(42)) shouldBe MySome(8 -> 42)
      Applicative[MyOption].product(MySome(8), MyNone) shouldBe MyNone
      Applicative[MyOption].product(MyNone, MySome(42)) shouldBe MyNone
      Applicative[MyOption].product(MyNone, MyNone) shouldBe MyNone
    }

    /* FIXME missing implicit
    "verify MyEither" in {
      import com.github.niqdev.Applicative.instances.myEitherApplicative
      import com.github.niqdev.Semigroup.instances.myListSemigroup

      type EitherMyListString[A] = MyEither[MyList[String], A]

      Applicative[EitherMyListString].pure(8)
    }
    */

    "verify Function1" in {
      // TODO not implemented
    }
  }

}
