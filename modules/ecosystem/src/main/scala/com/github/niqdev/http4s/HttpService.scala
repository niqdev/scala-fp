package com.github.niqdev.http4s

import cats.effect.Sync
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.http4sKleisliResponseSyntax
import org.http4s.{HttpApp, HttpRoutes}

private[http4s] sealed abstract class HttpService[F[_] : Sync] extends Http4sDsl[F] {

  // HttpRoutes[F] = Kleisli[OptionT[F, ?], Request[F], Response[F]]
  val helloWorldService: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        Ok(s"Hello $name!")
    }

  val endpoints: HttpApp[F] =
    helloWorldService.orNotFound
}

private[http4s] object HttpService {

  def apply[F[_] : Sync]: HttpService[F] =
    new HttpService[F] {}
}
