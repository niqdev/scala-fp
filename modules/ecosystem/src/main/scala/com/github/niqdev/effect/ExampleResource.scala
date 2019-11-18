package com.github.niqdev.effect

import cats.effect.{ ExitCode, IO, IOApp, Resource, Sync }
import cats.syntax.apply.catsSyntaxApply
import cats.syntax.functor.toFunctorOps

sealed abstract class MyResource[F[_]](implicit F: Sync[F]) {

  val acquire: F[String] =
    F.delay(println("acquire")) *> F.pure("[step-2|acquire]")

  val release: String => F[Unit] =
    _ => F.delay(println("release"))

  val transform: String => F[String] =
    x => F.delay(println("transform")) *> F.pure(x ++ "[step-3|transform]")

  val allocate: String => F[String] =
    x => F.delay(println("allocate")) *> F.pure("[step-1|allocate]" ++ x)

  def resource: F[String] =
    Resource
      .make(acquire)(release)
      .evalMap(transform)
      .use(allocate)
}

object MyResource {
  def apply[F[_]: Sync]: MyResource[F] = new MyResource[F] {}
}

object ExampleResource extends IOApp {

  def program: IO[Unit] =
    MyResource[IO].resource
      .flatMap(result => IO(println(result)))

  // acquire
  // transform
  // allocate
  // release
  // [step-1|allocate][step-2|acquire][step-3|transform]
  override def run(args: List[String]): IO[ExitCode] =
    program.as(ExitCode.Success)

}
