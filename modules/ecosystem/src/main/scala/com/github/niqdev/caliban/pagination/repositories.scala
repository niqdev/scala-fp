package com.github.niqdev.caliban.pagination

import cats.effect.Sync
import com.github.niqdev.caliban.pagination.models._

object repositories {

  sealed trait RepositoryI[F[_], T] {
    def findAll: F[List[T]]
    def findById(id: String): F[Option[T]]
  }

  /**
    *
    */
  sealed abstract class UserRepo[F[_]](implicit F: Sync[F]) extends RepositoryI[F, UserModel] {

    override def findAll: F[List[UserModel]] =
      F.pure(models.users)

    override def findById(id: String): F[Option[UserModel]] =
      F.pure(models.users.find(_.id == id))
  }
  object UserRepo {
    def apply[F[_]: Sync]: UserRepo[F] =
      new UserRepo[F]() {}
  }

  /**
    *
    */
  sealed abstract class RepositoryRepo[F[_]](implicit F: Sync[F]) extends RepositoryI[F, RepositoryModel] {

    override def findAll: F[List[RepositoryModel]] =
      F.pure(models.repositories)

    override def findById(id: String): F[Option[RepositoryModel]] =
      F.pure(models.repositories.find(_.id == id))

    def findAllByUserId(userId: String): F[List[RepositoryModel]] =
      F.pure(models.repositories.filter(_.userId == userId))

    def findByUserId(userId: String): F[Option[RepositoryModel]] =
      F.pure(models.repositories.find(_.userId == userId))
  }
  object RepositoryRepo {
    def apply[F[_]: Sync]: RepositoryRepo[F] =
      new RepositoryRepo[F]() {}
  }

  /**
    *
    */
  sealed trait Repositories[F[_]] {
    def userRepo: UserRepo[F]
    def repositoryRepo: RepositoryRepo[F]
  }
  def apply[F[_]: Sync]: Repositories[F] =
    new Repositories[F] {
      val userRepo: UserRepo[F]             = UserRepo[F]
      val repositoryRepo: RepositoryRepo[F] = RepositoryRepo[F]
    }
}
