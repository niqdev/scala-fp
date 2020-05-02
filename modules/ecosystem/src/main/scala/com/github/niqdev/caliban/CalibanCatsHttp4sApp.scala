package com.github.niqdev.caliban

import caliban.Http4sAdapter
import caliban.interop.cats.implicits.CatsEffectGraphQL
import cats.effect.{ ConcurrentEffect, ExitCode, IO, IOApp, Resource, Timer }
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli.http4sKleisliResponseSyntaxOptionT
import zio.Runtime

import scala.concurrent.ExecutionContext

object CalibanCatsHttp4sApp extends IOApp {

  implicit val runtime: Runtime[Any] = Runtime.default

  override def run(args: List[String]): IO[ExitCode] =
    server[IO]
      .use(_ => IO.never)
      .as(ExitCode.Success)

  def server[F[_]: ConcurrentEffect: Timer]: Resource[F, Unit] =
    for {
      interpreter <- Resource.liftF(ExampleApi.api.interpreterAsync)
      httpApp = Router(
        "/api/graphql" -> Http4sAdapter.makeHttpServiceF(interpreter)
      ).orNotFound
      _ <- BlazeServerBuilder[F](ExecutionContext.global)
        .bindLocal(8080)
        .withHttpApp(httpApp)
        .resource
    } yield ()
}
