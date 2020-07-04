package com.github.niqdev.caliban

import caliban.Http4sAdapter
import caliban.interop.cats.implicits.CatsEffectGraphQL
import cats.effect.{ ConcurrentEffect, ExitCode, IO, IOApp, Resource, Timer }
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli.http4sKleisliResponseSyntaxOptionT
import zio.Runtime

import scala.concurrent.ExecutionContext

object CalibanCatsHttp4sApp extends IOApp {

  implicit val runtime: Runtime[Any] = Runtime.default

  override def run(args: List[String]): IO[ExitCode] =
    Slf4jLogger
      .create[IO]
      .flatMap(implicit logger =>
        server[IO]
          .use(_ => IO.never)
          .as(ExitCode.Success)
      )

  def server[F[_]: ConcurrentEffect: Timer: Logger]: Resource[F, Unit] =
    for {
      _ <- Resource.liftF(Logger[F].info("Start server..."))
      api = ExampleApi.api |+| pagination.queries.api[F]
      _           <- Resource.liftF(Logger[F].info(s"GraphQL Schema:\n${api.render}"))
      interpreter <- Resource.liftF(api.interpreterAsync)
      httpApp = Router(
        "/api/graphql" -> Http4sAdapter.makeHttpServiceF(interpreter)
      ).orNotFound
      _ <- BlazeServerBuilder[F](ExecutionContext.global)
        .bindLocal(8080)
        .withHttpApp(httpApp)
        .resource
    } yield ()
}

/*

// TODO
query node {
  node(id: "userId") {
    id
    ... on User {
      id
      name
    }
    ... on Repository {
      id
      url
    }
  }
  getRepository: node(id: "repositoryId") {
    ... on Repository {
      id
      url
    }
  }
}

 */
