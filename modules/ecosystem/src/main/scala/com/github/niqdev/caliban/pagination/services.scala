package com.github.niqdev.caliban
package pagination

import java.nio.charset.StandardCharsets
import java.util.Base64

import cats.effect.Sync
import cats.instances.list._
import cats.instances.option._
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.foldable._
import cats.syntax.functor._
import cats.syntax.nested._
import com.github.niqdev.caliban.pagination.repositories._
import com.github.niqdev.caliban.pagination.schema._

import scala.util.Try

// TODO newtype + refined
object services {

  private[this] val fromBase64: String => String =
    value => String.valueOf(Base64.getDecoder.decode(value.getBytes(StandardCharsets.UTF_8)))

  private[this] val fromBase64WithoutPrefix: (String, String) => Try[Long] =
    (value, prefix) => Try(fromBase64(value).replace(prefix, "").toLong)

  /**
    *
    */
  sealed abstract class UserService[F[_]](
    userRepo: UserRepo[F],
    repositoryRepo: RepositoryRepo[F]
  )(
    implicit F: Sync[F]
  ) {

    private[this] def findRepositoryConnection(userId: Long): F[RepositoryConnection] = ???

    // TODO
    def findNode(id: String): F[Option[User]] =
      for {
        userId               <- F.fromTry(fromBase64WithoutPrefix(id, User.idPrefix))
        repositoryConnection <- findRepositoryConnection(userId)
        maybeUser            <- userRepo.findById(userId).nested.map(User.fromModel(repositoryConnection)).value
      } yield maybeUser

    // TODO
    def findByName(name: String): F[Option[User]] =
      (for {
        maybeUserModel       <- userRepo.findByName(name)
        userModel            <- F.fromOption(maybeUserModel, new IllegalArgumentException("invalid name"))
        repositoryConnection <- findRepositoryConnection(userModel.id)
        user                 <- F.pure(userModel).map(User.fromModel(repositoryConnection))
      } yield user).redeem(_ => None, Some(_))

  }
  object UserService {
    def apply[F[_]: Sync](userRepo: UserRepo[F], repositoryRepo: RepositoryRepo[F]): UserService[F] =
      new UserService[F](userRepo, repositoryRepo) {}
  }

  /**
    *
    */
  sealed abstract class RepositoryService[F[_]](
    repositoryRepo: RepositoryRepo[F]
  )(
    implicit F: Sync[F]
  ) {

    def findNode(id: String): F[Option[Repository]] =
      for {
        repositoryId    <- F.fromTry(fromBase64WithoutPrefix(id, Repository.idPrefix))
        maybeRepository <- repositoryRepo.findById(repositoryId).nested.map(Repository.fromModel).value
      } yield maybeRepository

    def findByName(name: String): F[Option[Repository]] =
      repositoryRepo.findByName(name).nested.map(Repository.fromModel).value

    // TODO mapN
    def connection(first: Long, after: String): F[RepositoryConnection] = ???
  }
  object RepositoryService {
    def apply[F[_]: Sync](repositoryRepo: RepositoryRepo[F]): RepositoryService[F] =
      new RepositoryService[F](repositoryRepo) {}
  }

  /**
    *
    */
  abstract class NodeService[F[_]: Sync](
    userService: UserService[F],
    repositoryService: RepositoryService[F]
  ) {
    def findNode(id: String): F[Option[Node]] =
      for {
        user       <- userService.findNode(id)
        repository <- repositoryService.findNode(id)
      } yield List(user, repository).foldK
  }
  object NodeService {
    def apply[F[_]: Sync](repositories: Repositories[F]): NodeService[F] =
      new NodeService[F](
        UserService[F](repositories.userRepo, repositories.repositoryRepo),
        RepositoryService[F](repositories.repositoryRepo)
      ) {}
  }
}
