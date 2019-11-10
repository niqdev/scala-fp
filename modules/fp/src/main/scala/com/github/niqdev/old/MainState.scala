package com.github.niqdev.old

import com.github.niqdev.old.internal.State

object MainState extends App {

  def updateCart(item: String): State[List[String], String] = State { cart =>
    val items = item :: cart
    (items, item)
  }

  val program: State[List[String], String] =
    for {
      _ <- updateCart("apples")
      _ <- updateCart("bananas")
      _ <- updateCart("chocolate")
      shoppingList <- updateCart("tomato")
    } yield shoppingList

  val unsafeResult = program.run(List.empty[String])

  // State[S, A] = [S=List(tomato, chocolate, bananas, apples)][A=tomato]
  println(s"State[S, A] = [S=${unsafeResult._1}][A=${unsafeResult._2}]")

}
