package com.github.niqdev

import com.github.niqdev.internal.{IO, MyConsole}

object MainIO extends App {

  val hello = MyConsole[IO].println("Hello World")

  val program: IO[Unit] =
    for {
      _ <- hello
      _ <- hello
    } yield ()

  program.run

}
