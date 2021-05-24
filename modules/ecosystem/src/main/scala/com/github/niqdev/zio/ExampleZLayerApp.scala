package com.github.niqdev.zio

import java.io.IOException

import com.github.niqdev.zio.Person.Person
import zio._
import zio.console._

object Person {
  type Person = Has[Person.Service]

  trait Service {
    def sayHello(name: String): IO[IOException, Unit]
    def sayGoodbye: IO[IOException, Unit]
  }
  object Service {
    // Console service dependency
    val live: Console.Service => Service =
      console =>
        new Service {
          override def sayHello(name: String): IO[IOException, Unit] =
            console.putStrLn(s"Hello $name!")

          override def sayGoodbye: IO[IOException, Unit] =
            console.putStrLn("Goodbye!")
        }
  }

  val live: ZLayer[Console, Nothing, Person] =
    ZLayer.fromService(Service.live)

  def sayHello(name: String): RIO[Person, Unit] =
    ZIO.accessM[Person](_.get.sayHello(name))

  def sayGoodbye: RIO[Person, Unit] =
    ZIO.accessM(_.get.sayGoodbye)
}

// https://zio.dev/docs/howto/howto_use_layers
// https://zio.dev/docs/datatypes/datatypes_zlayer
object ExampleZLayerApp extends App {

  // simplest layer possible
  private[this] val nameLayer = ZLayer.succeed("ZIO")
  // dependency graph
  private[this] val env = nameLayer ++ Console.live ++ Person.live

  private[this] val program: RIO[Person with Console with Has[String], Unit] =
    for {
      name <- ZIO.access[Has[String]](_.get) // access layer directly
      _    <- Person.sayHello(name) // access custom layer
      _    <- putStrLn("TODO") // access existing layer
      _    <- Person.sayGoodbye
    } yield ()

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    program.provideLayer(env).exitCode

}
