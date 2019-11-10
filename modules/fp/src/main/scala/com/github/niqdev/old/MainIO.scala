package com.github.niqdev.old

import com.github.niqdev.old.internal.{IO, MyConsole}

object MainIO extends App {

  val hello = MyConsole[IO].println("Hello World")

  val program: IO[Unit] =
    for {
      _ <- hello
      _ <- hello
    } yield ()

  program.run

}
