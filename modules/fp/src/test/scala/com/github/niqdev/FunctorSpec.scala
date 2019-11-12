package com.github.niqdev

import org.scalatest.{Matchers, WordSpecLike}

final class FunctorSpec extends WordSpecLike with Matchers {

  "Functor" should {

    "verify MyTree" in {
      import com.github.niqdev.datastructure.MyTree.instances.myTreeFunctor
      import com.github.niqdev.datastructure.MyTree.{branch, leaf}
      import com.github.niqdev.datastructure.{MyBranch, MyLeaf, MyTree}

      val inputTree = branch(leaf(10), leaf(20))
      val expectedTree = MyBranch(MyLeaf(30), MyLeaf(60))

      Functor[MyTree].map(inputTree)(_ * 3) shouldBe expectedTree

      import com.github.niqdev.Functor.syntax.FunctorOps
      // syntax
      inputTree.map(_ * 3) shouldBe expectedTree
    }

    "verify MyList" in {
      import com.github.niqdev.datastructure.MyList.instances.myListFunctor
      import com.github.niqdev.datastructure.MyList

      Functor[MyList].map(MyList(1, 2, 3, 4))(_ * 3) shouldBe MyList(3, 6, 9, 12)
    }

    "verify MyOption" in {
      import com.github.niqdev.datatype.MyOption.instances.myOptionFunctor
      import com.github.niqdev.datatype.{MyNone, MyOption, MySome}

      Functor[MyOption].map(MySome(21))(_ * 2) shouldBe MySome(42)
      Functor[MyOption].map(MyNone: MyOption[Int])(_ * 2) shouldBe MyNone
    }

    /* FIXME kind-projector
    "verify MyEither" in {
      import com.github.niqdev.datatype.MyEither.instances.myEitherFunctor
      import com.github.niqdev.datatype.{MyEither, MyLeft, MyRight}

      Functor[MyEither].map(MyRight(21))(_ * 2) shouldBe MyRight(42)
      Functor[MyEither].map(MyLeft("error"))(_ * 2) shouldBe MyLeft("error")
    }
    */

    /* FIXME kind-projector
    "verify Function1" in {
      import com.github.niqdev.Functor.instances.function1Functor

      val func1 = (a: Int) => a + 1
      val func2 = (a: Int) => a * 2
      val func3 = (a: Int) => a + "!"
      val func4 = func1.map(func2).map(func3)
    }
    */
  }

}
