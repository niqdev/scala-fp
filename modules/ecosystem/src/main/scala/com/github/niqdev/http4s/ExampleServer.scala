package com.github.niqdev.http4s

import cats.effect.{ ConcurrentEffect, ExitCode, IO, IOApp, Resource, Timer }
import cats.implicits.toSemigroupKOps
import cats.syntax.functor.toFunctorOps
import org.http4s.HttpRoutes
import org.http4s.syntax.kleisli.http4sKleisliResponseSyntaxOptionT
import org.http4s.metrics.prometheus.{ Prometheus, PrometheusExportService }
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Metrics

object ExampleServer extends IOApp {

  def server[F[_]: ConcurrentEffect: Timer]: Resource[F, ExitCode] =
    for {
      prometheusService <- Resource.liftF(PrometheusExportService.build[F])
      helloRoute: HttpRoutes[F] = HttpService[F].helloRoute
      meteredRoutes <- Resource
        .liftF(
          Prometheus[F](prometheusService.collectorRegistry, "server")
            .map(Metrics[F](_)(helloRoute))
        )
      allRoutes = meteredRoutes <+> prometheusService.routes
      httpApp   = Router("/" -> allRoutes).orNotFound
      exitCode <- Resource.liftF(
        BlazeServerBuilder[F]
          .bindLocal(8080)
          .withHttpApp(httpApp)
          .serve
          .compile
          .drain
          .as(ExitCode.Success)
      )
    } yield exitCode

  override def run(args: List[String]): IO[ExitCode] =
    server[IO].use(code => IO.pure(code))

}
