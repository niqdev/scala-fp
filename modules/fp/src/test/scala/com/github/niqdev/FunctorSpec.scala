package com.github.niqdev

import com.github.niqdev.datastructure.{MyBranch, MyLeaf, MyTree}
import org.scalatest.{Matchers, WordSpecLike}
import com.github.niqdev.datastructure.MyTree.{branch, leaf}

final class FunctorSpec extends WordSpecLike with Matchers {

  "Functor" should {

    "verify MyTree" in {
      import com.github.niqdev.datastructure.MyTree.instances.myTreeFunctor

      val inputTree = branch(leaf(10), leaf(20))
      val expectedTree = MyBranch(MyLeaf(30),MyLeaf(60))

      Functor[MyTree].map(inputTree)(_ * 3) shouldBe expectedTree

      import com.github.niqdev.Functor.syntax.FunctorOps
      // syntax
      inputTree.map(_ * 3) shouldBe expectedTree
    }
  }

}
