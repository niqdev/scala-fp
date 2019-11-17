package com.github.niqdev.http4s

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.functor.toFunctorOps
import org.http4s.dsl.io.{->, /, GET, Ok, Root, http4sOkSyntax}
import org.http4s.implicits.http4sKleisliResponseSyntax
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpApp, HttpRoutes}

object ExampleServer extends IOApp {

  // HttpRoutes[F] = Kleisli[OptionT[F, ?], Request[F], Response[F]]
  val helloWorldService: HttpApp[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "hello" / name =>
        Ok(s"Hello $name!")
    }.orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindLocal(8080)
      .withHttpApp(helloWorldService)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
