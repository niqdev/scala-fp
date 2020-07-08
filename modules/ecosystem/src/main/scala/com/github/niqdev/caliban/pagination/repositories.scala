package com.github.niqdev.caliban.pagination

import cats.effect.{ Resource, Sync }
import com.github.niqdev.caliban.pagination.models._
import doobie.syntax.all._
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor
import eu.timepit.refined.types.string.NonEmptyString

object repositories {

  import doobie.implicits.legacy.instant.JavaTimeInstantMeta
  import doobie.refined.implicits.refinedMeta

  /**
    *
    */
  sealed abstract class UserRepo[F[_]: Sync](xa: Transactor[F]) {

    def findAll: F[List[User]] =
      UserRepo.queries.findAll.query[User].to[List].transact(xa)

    def findById(id: UserId): F[Option[User]] =
      UserRepo.queries.findById(id).query[User].option.transact(xa)

    def findByName(name: NonEmptyString): F[Option[User]] =
      UserRepo.queries.findByName(name).query[User].option.transact(xa)

    def count: F[Long] =
      UserRepo.queries.count.query[Long].unique.transact(xa)
  }
  object UserRepo {
    def apply[F[_]: Sync](xa: Transactor[F]): UserRepo[F] =
      new UserRepo[F](xa) {}

    private[pagination] object queries {
      private[this] val schemaName = "example"
      private[this] val tableName  = "user"
      private[this] val tableFrom  = Fragment.const(s" FROM $schemaName.$tableName ")

      lazy val findAll: Fragment =
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

    def findAll: F[List[Repository]] =
      RepositoryRepo.queries.findAll.query[Repository].to[List].transact(xa)

    def findAllByUserId(userId: UserId): F[List[Repository]] =
      RepositoryRepo.queries.findAllByUserId(userId).query[Repository].to[List].transact(xa)

    def findById(id: RepositoryId): F[Option[Repository]] =
      RepositoryRepo.queries.findById(id).query[Repository].option.transact(xa)

    def findByName(name: NonEmptyString): F[Option[Repository]] =
      RepositoryRepo.queries.findByName(name).query[Repository].option.transact(xa)

    def count: F[Long] =
      RepositoryRepo.queries.count.query[Long].unique.transact(xa)

    def countByUserId(userId: UserId): F[Long] =
      RepositoryRepo.queries.countByUserId(userId).query[Long].unique.transact(xa)
  }
  object RepositoryRepo {
    def apply[F[_]: Sync](xa: Transactor[F]): RepositoryRepo[F] =
      new RepositoryRepo[F](xa) {}

    private[pagination] object queries {
      private[this] val schemaName = "example"
      private[this] val tableName  = "repository"
      private[this] val tableFrom  = Fragment.const(s" FROM $schemaName.$tableName ")

      lazy val findAll: Fragment =
        fr"SELECT id, user_id, name, url, is_fork, created_at, updated_at" ++ tableFrom

      lazy val findAllByUserId: UserId => Fragment =
        userId => findAll ++ fr"WHERE user_id = $userId"

      lazy val findById: RepositoryId => Fragment =
        id => findAll ++ fr"WHERE id = $id"

      lazy val findByName: NonEmptyString => Fragment =
        name => findAll ++ fr"WHERE name = $name"

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
