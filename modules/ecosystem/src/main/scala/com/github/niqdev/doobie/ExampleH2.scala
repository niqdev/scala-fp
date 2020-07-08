package com.github.niqdev.doobie

import java.time.Instant
import java.util.UUID

import cats.effect._
import doobie.h2.H2Transactor
import doobie.syntax.all._
import doobie.util.ExecutionContexts
import doobie.util.meta.Meta
import doobie.util.transactor.Transactor
import eu.timepit.refined.types.string.NonEmptyString
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.flywaydb.core.Flyway

// https://tpolecat.github.io/doobie/book
object ExampleH2 extends IOApp {

  import doobie.implicits.legacy.instant.JavaTimeInstantMeta
  import doobie.refined.implicits.refinedMeta

  implicit val idMeta: Meta[UUID] = Meta.Advanced.other[UUID]("id")

  private[this] final case class DatabaseConfig(
    connectionUrl: String,
    username: String,
    password: String,
    schema: String
  )

  private[this] def migration[F[_]](config: DatabaseConfig)(implicit F: Sync[F]): F[Int] =
    F.delay(
      Flyway
        .configure()
        .schemas(config.schema)
        .defaultSchema(config.schema)
        .dataSource(config.connectionUrl, config.username, config.password)
        .load()
        .migrate()
    )

  private[this] def transactor[F[_]: Async: ContextShift](
    config: DatabaseConfig
  ): Resource[F, H2Transactor[F]] =
    for {
      ec         <- ExecutionContexts.fixedThreadPool[F](32)
      blockingEC <- Blocker[F]
      xa <- H2Transactor.newH2Transactor[F](
        url = config.connectionUrl,
        user = config.username,
        pass = config.password,
        connectEC = ec,
        blocker = blockingEC
      )
    } yield xa

  @scala.annotation.nowarn
  private[this] def findUsers[F[_]](xa: Transactor[F])(
    implicit ev: Bracket[F, Throwable]
  ): F[List[(UUID, NonEmptyString, Instant, Instant)]] =
    sql"select id, name, created_at, updated_at from example.user"
      .query[(UUID, NonEmptyString, Instant, Instant)]
      .to[List]
      .transact(xa)

  private[this] def h2Example[F[_]: Async: ContextShift: Logger]: Resource[F, Unit] = {
    val config = DatabaseConfig("jdbc:h2:mem:example_db;DB_CLOSE_DELAY=-1", "sa", "", "example")

    for {
      version <- Resource.liftF(migration[F](config))
      _       <- Resource.liftF(Logger[F].info(s"migration version: $version"))
      xa      <- transactor[F](config)
      users   <- Resource.liftF(findUsers[F](xa))
      _       <- Resource.liftF(Logger[F].info(s"findUsers: $users"))
    } yield ()
  }

  def run(args: List[String]): IO[ExitCode] =
    Slf4jLogger.create[IO].flatMap(implicit logger => h2Example[IO].use(_ => IO.pure(ExitCode.Success)))

}
