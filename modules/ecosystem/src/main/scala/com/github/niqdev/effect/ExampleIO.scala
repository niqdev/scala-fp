package com.github.niqdev.effect

import cats.effect.IO

object ExampleIO extends App {

  val ioa = IO {
    println("hello")
  }

  val program = for {
    _ <- ioa
    _ <- ioa
  } yield ()

  program.unsafeRunSync()
}
