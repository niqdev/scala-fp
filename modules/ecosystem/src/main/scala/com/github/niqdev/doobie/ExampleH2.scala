package com.github.niqdev.doobie

import java.net.URL
import java.time.Instant
import java.util.UUID

import cats.effect._
import doobie.syntax.all._
import doobie.util.meta.Meta
import doobie.util.transactor.Transactor
import eu.timepit.refined.types.numeric.PosLong
import eu.timepit.refined.types.string.NonEmptyString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

// https://tpolecat.github.io/doobie/book
object ExampleH2 extends IOApp {

  import doobie.implicits.legacy.instant.JavaTimeInstantMeta
  import doobie.refined.implicits.refinedMeta

  implicit val logHandler = doobie.util.log.LogHandler.jdkLogHandler
  implicit val idMeta     = Meta.Advanced.other[UUID]("id")
  implicit val urlMeta    = Meta.StringMeta.timap(new URL(_))(_.toString)

  private[this] def countUsers[F[_]](xa: Transactor[F])(
    implicit ev: Bracket[F, Throwable]
  ): F[Int] =
    sql"select count(*) from example.user"
      .query[Int]
      .unique
      .transact(xa)

  @scala.annotation.nowarn
  private[this] def findRepositories[F[_]](xa: Transactor[F])(
    implicit ev: Bracket[F, Throwable]
  ): F[List[(PosLong, UUID, UUID, NonEmptyString, URL, Boolean, Instant, Instant)]] =
    sql"select ROWNUM(), id, user_id, name, url, is_fork, created_at, updated_at from example.repository"
      .query[(PosLong, UUID, UUID, NonEmptyString, URL, Boolean, Instant, Instant)]
      .to[List]
      .transact(xa)

  private[this] def h2Example[F[_]: Async: ContextShift: Logger]: Resource[F, Unit] =
    for {
      xa           <- Database.initInMemory[F]
      userCount    <- Resource.eval(countUsers[F](xa))
      _            <- Resource.eval(Logger[F].info(s"countUsers: $userCount"))
      repositories <- Resource.eval(findRepositories[F](xa))
      _            <- Resource.eval(Logger[F].info(s"findRepositories: $repositories"))
    } yield ()

  def run(args: List[String]): IO[ExitCode] =
    Slf4jLogger.create[IO].flatMap(implicit logger => h2Example[IO].use(_ => IO.pure(ExitCode.Success)))

}
