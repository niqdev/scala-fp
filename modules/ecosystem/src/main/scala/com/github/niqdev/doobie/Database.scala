package com.github.niqdev.doobie

import cats.effect.{ Async, Blocker, ContextShift, Resource, Sync }
import doobie.h2.H2Transactor
import doobie.util.ExecutionContexts
import io.chrisdavenport.log4cats.Logger
import org.flywaydb.core.Flyway

object Database {

  final case class Config(
    connectionUrl: String,
    username: String,
    password: String,
    schema: String
  )

  private[this] def migration[F[_]](config: Config)(implicit F: Sync[F]): F[Int] =
    F.delay(
      Flyway
        .configure()
        .schemas(config.schema)
        .defaultSchema(config.schema)
        .dataSource(config.connectionUrl, config.username, config.password)
        .load()
        .migrate()
        .migrationsExecuted
    )

  private[this] def transactor[F[_]: Async: ContextShift](config: Config): Resource[F, H2Transactor[F]] =
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

  // http://h2database.com/html/main.html
  def initInMemory[F[_]: Async: ContextShift: Logger]: Resource[F, H2Transactor[F]] = {
    val config = Config("jdbc:h2:mem:example_db;DB_CLOSE_DELAY=-1", "sa", "", "example")

    for {
      _       <- Resource.eval(Logger[F].info(s"Init in-memory database..."))
      _       <- Resource.eval(Logger[F].info(s"config: $config"))
      version <- Resource.eval(migration[F](config))
      _       <- Resource.eval(Logger[F].info(s"migration version: $version"))
      xa      <- transactor[F](config)
    } yield xa
  }
}
