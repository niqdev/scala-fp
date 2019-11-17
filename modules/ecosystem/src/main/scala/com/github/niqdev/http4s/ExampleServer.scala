package com.github.niqdev.http4s

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.functor.toFunctorOps
import org.http4s.server.blaze.BlazeServerBuilder

object ExampleServer extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindLocal(8080)
      .withHttpApp(HttpService[IO].endpoints)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
