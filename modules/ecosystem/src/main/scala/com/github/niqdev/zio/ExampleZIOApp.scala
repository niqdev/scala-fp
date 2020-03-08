package com.github.niqdev.zio

import java.io.IOException

import zio.console.{ Console, getStrLn, putStrLn }
import zio.{ App, ZIO }

object ExampleZIOApp extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    myApp.fold(_ => 1, _ => 0)

  final val myApp: ZIO[Console, IOException, Unit] =
    for {
      _     <- putStrLn("hello")
      value <- getStrLn
      _     <- putStrLn(s"value: $value")
    } yield ()
}
