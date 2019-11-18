package com.github.niqdev
package main

object MainState {

  def updateListString(item: String): State[List[String], String] =
    State { oldList: List[String] =>
      val newList = item :: oldList
      (newList, item)
    }

  val program: State[List[String], String] =
    for {
      _            <- updateListString("apple")
      _            <- updateListString("banana")
      _            <- updateListString("pear")
      shoppingList <- updateListString("strawberry")
    } yield shoppingList

  def main(args: Array[String]): Unit = {
    val result = program.get(List.empty[String])

    // State[S, A] = [S=List(strawberry, pear, banana, apple)][A=strawberry]
    println(s"State[S, A] = [S=${result._1}][A=${result._2}]")
  }
}
