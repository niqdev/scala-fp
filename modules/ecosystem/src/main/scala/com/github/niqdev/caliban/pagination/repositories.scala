package com.github.niqdev.caliban.pagination

import cats.effect.Sync
import cats.syntax.functor._
import com.github.niqdev.caliban.pagination.models._

object repositories {

  sealed abstract class RepositoryI[F[_], T <: Model](implicit F: Sync[F]) {
    def findAll: F[List[T]]
    def findById(id: String): F[Option[T]] =
      findAll.map(_.find(_.id == id))
    def count: F[Long] =
      findAll.map(_.length)
  }

  /**
    *
    */
  sealed abstract class UserRepo[F[_]](implicit F: Sync[F]) extends RepositoryI[F, UserModel] {

    override def findAll: F[List[UserModel]] =
      F.pure(models.users)

    def findByName(name: String): F[Option[UserModel]] =
      findAll.map(_.find(_.name == name))
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

    def findAllByUserId(userId: String): F[List[RepositoryModel]] =
      findAll.map(_.filter(_.userId == userId))

    def findByUserId(userId: String): F[Option[RepositoryModel]] =
      findAll.map(_.find(_.userId == userId))

    def findByName(name: String): F[Option[RepositoryModel]] =
      findAll.map(_.find(_.name == name))

    def countByUserId(userId: String): F[Long] =
      findAllByUserId(userId).map(_.length)
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
