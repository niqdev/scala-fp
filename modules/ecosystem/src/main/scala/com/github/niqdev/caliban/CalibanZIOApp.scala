package com.github.niqdev.caliban

import zio.console.putStrLn
import zio.{ ExitCode, URIO }

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

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    (for {
      interpreter <- ExampleApi.api.interpreter
      result      <- interpreter.execute(query)
      _           <- putStrLn(s"${result.data}")
    } yield ()).exitCode
}
