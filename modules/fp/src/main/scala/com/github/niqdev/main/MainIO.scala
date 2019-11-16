package com.github.niqdev
package main

import com.github.niqdev.Console.instances.ioConsole
import com.github.niqdev.IO.instances.ioMonad
import com.github.niqdev.Monad.syntax.MonadOps
import com.github.niqdev.Random.instances.ioRandom

object MainIO {

  // Console and Random use the Monad syntax in for-comprehensions i.e. flatMap + map
  def mainF[F[_] : Monad : Console : Random](args: List[String]): F[Unit] =
    for {
      _ <- Console[F].putStrLn("hello functional programming")
      n <- Random[F].nextInt(42)
      _ <- Console[F].putStrLn(s"random number: $n")
    } yield ()


  def main(args: Array[String]): Unit =
    mainF[IO](args.toList).unsafeRun
}
