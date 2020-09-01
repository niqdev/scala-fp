package com.github.niqdev.zio

import zio._
import zio.console._

object Person {
  type Person = Has[Person.Service]

  trait Service {
    def sayHello(name: String): UIO[Unit]
    def sayGoodbye: UIO[Unit]
  }

  object Service {
    // Console service dependency
    val live: Console.Service => Service =
      console =>
        new Service {
          override def sayHello(name: String): UIO[Unit] =
            console.putStrLn(s"Hello $name!")

          override def sayGoodbye: UIO[Unit] =
            console.putStrLn("Goodbye!")
        }
  }

  val live: ZLayer[Console, Nothing, Person] =
    ZLayer.fromService(Service.live)

  def sayHello(name: String): URIO[Person, Unit] =
    ZIO.accessM[Person](_.get.sayHello(name))

  def sayGoodbye: URIO[Person, Unit] =
    ZIO.accessM(_.get.sayGoodbye)
}

// https://zio.dev/docs/howto/howto_use_layers
// https://zio.dev/docs/datatypes/datatypes_zlayer
object ExampleZLayerApp extends App {

  // simplest layer possible
  private[this] val nameLayer = ZLayer.succeed("ZIO")
  // dependency graph
  private[this] val env = nameLayer ++ Console.live ++ Person.live

  private[this] val program =
    for {
      name <- ZIO.access[Has[String]](_.get)
      _    <- Person.sayHello(name)
      _    <- putStrLn("TODO")
      _    <- Person.sayGoodbye
    } yield ()

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    program.provideLayer(env).exitCode

}
