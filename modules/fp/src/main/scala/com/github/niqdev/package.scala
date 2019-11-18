package com.github

import com.github.niqdev.ShowSyntax

package object niqdev {

  // identity monad
  type Id[A] = A

  object all  extends AllSyntax
  object show extends ShowSyntax

  type MyList[A]      = com.github.niqdev.datastructure.MyList[A]
  type MyTree[A]      = com.github.niqdev.datastructure.MyTree[A]
  type MyOption[A]    = com.github.niqdev.datatype.MyOption[A]
  type MyEither[E, A] = com.github.niqdev.datatype.MyEither[E, A]

  val MyList   = com.github.niqdev.datastructure.MyList
  val MyCons   = com.github.niqdev.datastructure.MyCons
  val MyNil    = com.github.niqdev.datastructure.MyNil
  val MyTree   = com.github.niqdev.datastructure.MyTree
  val MyBranch = com.github.niqdev.datastructure.MyBranch
  val MyLeaf   = com.github.niqdev.datastructure.MyLeaf
  val MyOption = com.github.niqdev.datatype.MyOption
  val MySome   = com.github.niqdev.datatype.MySome
  val MyNone   = com.github.niqdev.datatype.MyNone
  val MyLeft   = com.github.niqdev.datatype.MyLeft
  val MyRight  = com.github.niqdev.datatype.MyRight
}

trait AllSyntax extends ShowSyntax
