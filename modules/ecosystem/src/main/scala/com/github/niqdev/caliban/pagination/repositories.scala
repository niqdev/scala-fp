package com.github.niqdev.caliban.pagination

import cats.effect.{ Resource, Sync }
import com.github.niqdev.caliban.pagination.models._
import doobie.syntax.all._
import doobie.util.fragment.Fragment
import doobie.util.meta.Meta
import doobie.util.transactor.Transactor
import eu.timepit.refined.types.numeric.{ NonNegInt, NonNegLong, PosLong }
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.Coercible
import io.estatico.newtype.macros.newtype

@scala.annotation.nowarn
object repositories {

  import doobie.implicits.legacy.instant.JavaTimeInstantMeta
  import doobie.refined.implicits.refinedMeta
  import doobie.h2.implicits.UuidType

  // enable default logging
  private[this] implicit val logHandler =
    doobie.util.log.LogHandler.jdkLogHandler

  // newtype meta
  private[this] implicit def coercibleMeta[R, N](
    implicit ev: Coercible[Meta[R], Meta[N]],
    R: Meta[R]
  ): Meta[N] = ev(R)

  @newtype case class RowNumber(value: PosLong)
  @newtype case class Limit(value: NonNegInt)

  /**
    *
    */
  sealed abstract class UserRepo[F[_]: Sync](xa: Transactor[F]) {

    def findById(id: UserId): F[Option[User]] =
      UserRepo.queries.findById(id).query[User].option.transact(xa)

    def findByName(name: NonEmptyString): F[Option[User]] =
      UserRepo.queries.findByName(name).query[User].option.transact(xa)

    def count: F[NonNegLong] =
      UserRepo.queries.count.query[NonNegLong].unique.transact(xa)
  }
  object UserRepo {
    def apply[F[_]: Sync](xa: Transactor[F]): UserRepo[F] =
      new UserRepo[F](xa) {}

    private[pagination] object queries {
      private[this] val schemaName = "example"
      private[this] val tableName  = "user"
      private[this] val tableFrom  = Fragment.const(s" FROM $schemaName.$tableName ")

      private[this] lazy val findAll: Fragment =
        fr"SELECT id, name, created_at, updated_at" ++ tableFrom

      lazy val findById: UserId => Fragment =
        id => findAll ++ fr"WHERE id = $id"

      lazy val findByName: NonEmptyString => Fragment =
        name => findAll ++ fr"WHERE name = $name"

      lazy val count: Fragment =
        fr"SELECT COUNT(*)" ++ tableFrom
    }
  }

  /**
    *
    */
  sealed abstract class RepositoryRepo[F[_]: Sync](xa: Transactor[F]) {

    def find(limit: Limit, nextRowNumber: Option[RowNumber]): F[List[(Repository, RowNumber)]] =
      RepositoryRepo.queries
        .find(limit, nextRowNumber)
        .query[(Repository, RowNumber)]
        .to[List]
        .transact(xa)

    def findByUserId(limit: Limit, nextRowNumber: Option[RowNumber])(
      userId: UserId
    ): F[List[(Repository, RowNumber)]] =
      RepositoryRepo.queries
        .findByUserId(limit, nextRowNumber)(userId)
        .query[(Repository, RowNumber)]
        .to[List]
        .transact(xa)

    def findById(id: RepositoryId): F[Option[Repository]] =
      RepositoryRepo.queries.findById(id).query[Repository].option.transact(xa)

    def findByName(name: NonEmptyString): F[Option[Repository]] =
      RepositoryRepo.queries.findByName(name).query[Repository].option.transact(xa)

    def count: F[NonNegLong] =
      RepositoryRepo.queries.count.query[NonNegLong].unique.transact(xa)

    def countByUserId(userId: UserId): F[NonNegLong] =
      RepositoryRepo.queries.countByUserId(userId).query[NonNegLong].unique.transact(xa)
  }
  object RepositoryRepo {
    def apply[F[_]: Sync](xa: Transactor[F]): RepositoryRepo[F] =
      new RepositoryRepo[F](xa) {}

    // TODO orderBy: default updated_at
    private[pagination] object queries {
      private[this] val schemaName = "example"
      private[this] val tableName  = "repository"
      private[this] val tableFrom  = Fragment.const(s" FROM $schemaName.$tableName ")
      private[this] val columns    = Fragment.const(s"id, user_id, name, url, is_fork, created_at, updated_at")

      private[this] def findAll(
        extraColumns: Option[Fragment] = None,
        where: Option[Fragment] = None
      ): Fragment =
        fr"SELECT " ++ columns ++ extraColumns.getOrElse(fr"") ++ tableFrom ++ where.getOrElse(fr"") ++ fr" ORDER BY updated_at"

      private[this] def find(
        limit: Limit,
        nextRowNumber: Option[RowNumber],
        where: Option[Fragment]
      ): Fragment = {
        val rowNumberColumn     = Fragment.const(s", ROW_NUMBER() OVER (ORDER BY updated_at) AS row_number")
        val findLimit: Fragment = findAll(Some(rowNumberColumn), where) ++ fr" LIMIT $limit"
        val findLimitAfterRowNumber: RowNumber => Fragment = rowNumber =>
          fr"SELECT * FROM (" ++ findAll(Some(rowNumberColumn), where) ++ fr") t WHERE t.row_number > $rowNumber" ++ fr" LIMIT $limit"

        nextRowNumber.fold(findLimit)(findLimitAfterRowNumber)
      }

      def find(limit: Limit, nextRowNumber: Option[RowNumber]): Fragment =
        find(limit, nextRowNumber, None)

      def findByUserId(limit: Limit, nextRowNumber: Option[RowNumber]): UserId => Fragment =
        userId => find(limit, nextRowNumber, Some(fr"WHERE user_id = $userId"))

      lazy val findById: RepositoryId => Fragment =
        id => findAll(where = Some(fr"WHERE id = $id"))

      lazy val findByName: NonEmptyString => Fragment =
        name => findAll(where = Some(fr"WHERE name = $name"))

      lazy val count: Fragment =
        fr"SELECT COUNT(*)" ++ tableFrom

      lazy val countByUserId: UserId => Fragment =
        userId => fr"SELECT COUNT(*)" ++ tableFrom ++ fr"WHERE user_id = $userId"
    }
  }

  /**
    *
    */
  sealed trait Repositories[F[_]] {
    def userRepo: UserRepo[F]
    def repositoryRepo: RepositoryRepo[F]
  }
  object Repositories {
    private[this] def apply[F[_]: Sync](xa: Transactor[F]): Repositories[F] =
      new Repositories[F] {
        val userRepo: UserRepo[F]             = UserRepo[F](xa)
        val repositoryRepo: RepositoryRepo[F] = RepositoryRepo[F](xa)
      }

    def make[F[_]: Sync](xa: Transactor[F]): Resource[F, Repositories[F]] =
      Resource.liftF(Sync[F].delay(apply[F](xa)))
  }
}
