package com.github.niqdev.caliban
package pagination

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

// TODO newtype + refined
object services {

  // TODO remove
  private[this] val decodeNodeId: String => scala.util.Try[Long] =
    value => utils.longFromBase64(value, User.idPrefix, Repository.idPrefix)

  /**
    *
    */
  sealed abstract class UserService[F[_]](
    userRepo: UserRepo[F],
    repositoryRepo: RepositoryRepo[F]
  )(
    implicit F: Sync[F]
  ) {

    private[this] def findRepositoryConnection(userId: Long): F[RepositoryConnection] =
      for {
        // TODO use + return paging info
        repositories <- repositoryRepo.findAllByUserId(userId)
        edges        <- F.pure(repositories.map(RepositoryEdge.fromRepositoryModel))
        nodes        <- F.pure(repositories.map(Repository.fromModel))
        pageInfo <- F.pure {
          // TODO https://relay.dev/graphql/connections.htm
          PageInfo(true, true, "startCursor", "endCursor")
        }
        totalCount <- repositoryRepo.countByUserId(userId)
      } yield RepositoryConnection(edges, nodes, pageInfo, totalCount)

    def findNode(id: String): F[Option[User]] =
      for {
        userId               <- F.fromTry(decodeNodeId(id))
        repositoryConnection <- findRepositoryConnection(userId)
        maybeUser            <- userRepo.findById(userId).nested.map(User.fromModel(repositoryConnection)).value
      } yield maybeUser

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
        repositoryId    <- F.fromTry(decodeNodeId(id))
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

    // TODO bug: returns always user
    // TODO invoke service based on prefix starts with
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
