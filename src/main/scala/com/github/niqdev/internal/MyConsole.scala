package com.github.niqdev
package internal

trait MyConsole[F[_]] {
  def println[A: Show](a: A): F[Unit]
  def readLine(): F[String]
}

object MyConsole {

  def apply[F[_]](implicit F: MyConsole[F]): MyConsole[F] = F

  implicit def myConsoleIo: MyConsole[IO] =
    new MyConsole[IO] {
      override def println[A: Show](a: A): IO[Unit] =
        IO(scala.Console.println(a))

      override def readLine(): IO[String] =
        IO(scala.io.StdIn.readLine())
    }
}
