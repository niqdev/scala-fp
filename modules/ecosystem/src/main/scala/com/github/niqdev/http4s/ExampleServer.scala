package com.github.niqdev.http4s

import cats.effect.{ ConcurrentEffect, ExitCode, IO, IOApp, Resource, Timer }
import cats.syntax.all._
import org.http4s.HttpRoutes
import org.http4s.syntax.kleisli.http4sKleisliResponseSyntaxOptionT
import org.http4s.metrics.prometheus.{ Prometheus, PrometheusExportService }
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Metrics

import scala.concurrent.ExecutionContext

object ExampleServer extends IOApp {

  def server[F[_]: ConcurrentEffect: Timer]: Resource[F, ExitCode] =
    for {
      prometheusService <- PrometheusExportService.build[F]
      helloRoute: HttpRoutes[F] = HttpService[F].helloRoute
      meteredRoutes <-
        Prometheus
          .metricsOps[F](prometheusService.collectorRegistry, "server")
          .map(Metrics[F](_)(helloRoute))
      allRoutes = meteredRoutes <+> prometheusService.routes
      httpApp   = Router("/" -> allRoutes).orNotFound
      exitCode <- Resource.eval(
        BlazeServerBuilder[F](ExecutionContext.global)
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
