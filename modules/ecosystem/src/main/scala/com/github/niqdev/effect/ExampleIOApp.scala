package com.github.niqdev.effect

import cats.effect.{ ExitCode, IO, IOApp }

object ExampleIOApp extends IOApp {

  def program: IO[Unit] =
    for {
      _ <- IO(println("hello"))
      _ <- IO(println("world"))
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    program.as(ExitCode.Success)
}
