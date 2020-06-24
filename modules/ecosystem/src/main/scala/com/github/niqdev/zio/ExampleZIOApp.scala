package com.github.niqdev.zio

import java.io.IOException

import zio.console.{ Console, getStrLn, putStrLn }
import zio.{ App, ExitCode, URIO, ZIO }

object ExampleZIOApp extends App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    myApp.exitCode

  final val myApp: ZIO[Console, IOException, Unit] =
    for {
      _     <- putStrLn("hello")
      value <- getStrLn
      _     <- putStrLn(s"value: $value")
    } yield ()
}
