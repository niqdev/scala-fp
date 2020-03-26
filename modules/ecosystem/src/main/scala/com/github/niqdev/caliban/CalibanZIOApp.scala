package com.github.niqdev.caliban

import zio.console.putStrLn
import zio.{ UIO, ZIO }

object CalibanZIOApp extends zio.App {

  private[this] val query =
    """
      |{
      |  models {
      |    id
      |  }
      |  model(id: "model-8") {
      |    id
      |    description
      |    count
      |    valid
      |  }
      |}
      |""".stripMargin

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    (for {
      interpreter <- ExampleApi.api.interpreter
      result      <- interpreter.execute(query)
      _           <- putStrLn(s"${result.data}")
    } yield ())
      .foldM(
        error => putStrLn(s"$error") *> UIO.succeed(1),
        _ => UIO.succeed(0)
      )
}
