package com.github.niqdev

trait Random[F[_]] {
  def nextInt(n: Int): F[Int]
}

object Random {

  object instances extends RandomInstances

  def apply[F[_]](implicit ev: Random[F]): Random[F] = ev
}

trait RandomInstances {

  implicit val ioRandom: Random[IO] =
    (n: Int) => IO(scala.util.Random.nextInt(n))
}