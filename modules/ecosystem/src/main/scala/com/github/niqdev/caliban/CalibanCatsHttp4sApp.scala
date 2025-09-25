package com.github.niqdev.caliban

import caliban.Http4sAdapter
import caliban.interop.cats.implicits.CatsEffectGraphQL
import cats.effect.{ ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer }
import com.github.niqdev.caliban.pagination.queries.Queries
import com.github.niqdev.caliban.pagination.repositories.Repositories
import com.github.niqdev.caliban.pagination.services.Services
import com.github.niqdev.doobie.Database
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.http4s.server.Router
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.syntax.kleisli.http4sKleisliResponseSyntaxOptionT
import zio.Runtime

import scala.concurrent.ExecutionContext

// sbt -jvm-debug 5005 "ecosystem/runMain com.github.niqdev.caliban.CalibanCatsHttp4sApp"
object CalibanCatsHttp4sApp extends IOApp {

  private[this] implicit val runtime: Runtime[Any] = Runtime.default

  override def run(args: List[String]): IO[ExitCode] =
    Slf4jLogger
      .create[IO]
      .flatMap(implicit logger =>
        server[IO]
          .use(_ => IO.never)
          .as(ExitCode.Success)
      )

  private[caliban] def server[F[_]: ConcurrentEffect: ContextShift: Timer: Logger]: Resource[F, Unit] =
    for {
      _            <- Resource.eval(Logger[F].info("Start server..."))
      xa           <- Database.initInMemory[F]
      repositories <- Repositories.make[F](xa)
      services     <- Services.make[F](repositories)
      api = ExampleApi.api |+| Queries.api[F](services)
      _           <- Resource.eval(Logger[F].info(s"GraphQL Schema:\n${api.render}"))
      interpreter <- Resource.eval(api.interpreterAsync)
      httpApp = Router(
        "/api/graphql" -> Http4sAdapter.makeHttpServiceF(interpreter)
      ).orNotFound
      _ <- BlazeServerBuilder[F](ExecutionContext.global)
        .bindLocal(8080)
        .withHttpApp(httpApp)
        .resource
    } yield ()
}
