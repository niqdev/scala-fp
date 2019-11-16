package com.github.niqdev

trait Console[F[_]] {
  def putStrLn[A: Show](a: A): F[Unit]
  def getStrLn: F[String]
}

object Console {

  object instances extends ConsoleInstances

  def apply[F[_] : Console]: Console[F] =
    implicitly[Console[F]]
}

trait ConsoleInstances {

  import com.github.niqdev.show.ShowOps

  implicit val ioConsole: Console[IO] =
    new Console[IO] {
      override def putStrLn[A: Show](a: A): IO[Unit] =
        IO(scala.Console.println(a.show))

      override def getStrLn: IO[String] =
        IO(scala.io.StdIn.readLine())
    }
}