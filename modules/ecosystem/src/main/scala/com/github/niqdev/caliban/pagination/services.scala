package com.github.niqdev.caliban
package pagination

import cats.effect.{ Resource, Sync }
import cats.instances.list._
import cats.instances.option._
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.foldable._
import cats.syntax.functor._
import cats.syntax.nested._
import com.github.niqdev.caliban.pagination.codecs._
import com.github.niqdev.caliban.pagination.models._
import com.github.niqdev.caliban.pagination.repositories._
import com.github.niqdev.caliban.pagination.schema._
import com.github.niqdev.caliban.pagination.schema.arguments.ForwardPaginationArg
import eu.timepit.refined.types.string.NonEmptyString

object services {

  /**
    *
    */
  sealed abstract class UserService[F[_]](
    userRepo: UserRepo[F],
    repositoryRepo: RepositoryRepo[F]
  )(
    implicit F: Sync[F]
  ) {

    def findNode(id: NodeId): F[Option[UserNode[F]]] =
      for {
        userId <- F.fromEither(SchemaDecoder[NodeId, UserId].to(id))
        maybeUserNode <- userRepo
          .findById(userId)
          .nested
          .map(user => (user, findRepositoryConnection(user.id)).encodeFrom[UserNode[F]])
          .value
      } yield maybeUserNode

    def findByName(name: NonEmptyString): F[Option[UserNode[F]]] =
      (for {
        maybeUser <- userRepo.findByName(name)
        user      <- F.fromOption(maybeUser, new IllegalArgumentException("invalid name"))
        userNode  <- F.pure(user -> findRepositoryConnection(user.id)).map(_.encodeFrom[UserNode[F]])
      } yield userNode).redeem(_ => None, Some(_))

    // TODO move in repositoryRepo
    // TODO https://relay.dev/graphql/connections.htm
    private[this] def findRepositoryConnection(
      userId: UserId
    ): ForwardPaginationArg => F[RepositoryConnection[F]] =
      paginationArg =>
        for {
          repositories <- repositoryRepo.findAllByUserId(userId)
          edges        <- F.pure(repositories.map(_.encodeFrom[RepositoryEdge[F]]))
          nodes        <- F.pure(repositories.map(_._2.encodeFrom[RepositoryNode[F]]))
          pageInfo <- F.pure {
            PageInfo(
              true,
              true,
              Cursor(Base64String.unsafeFrom("aGVsbG8K")),
              Cursor(Base64String.unsafeFrom("aGVsbG8K"))
            )
          }
          totalCount <- repositoryRepo.countByUserId(userId)
        } yield RepositoryConnection(edges, nodes, pageInfo, totalCount)

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

    def findNode(id: NodeId): F[Option[RepositoryNode[F]]] =
      F.fromEither(SchemaDecoder[NodeId, RepositoryId].to(id))
        .flatMap(repositoryRepo.findById)
        .nested
        .map(_.encodeFrom[RepositoryNode[F]])
        .value

    def findByName(name: NonEmptyString): F[Option[RepositoryNode[F]]] =
      repositoryRepo.findByName(name).nested.map(_.encodeFrom[RepositoryNode[F]]).value

    def connection(first: Offset, after: Option[Cursor]): F[RepositoryConnection[F]] = ???

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

    // TODO create specific error + log WARN
    private[this] def recoverInvalidNode[T <: Node[F]]: PartialFunction[Throwable, Option[T]] = {
      case _: IllegalArgumentException => None
    }

    def findNode(id: NodeId): F[Option[Node[F]]] =
      for {
        userNode       <- userService.findNode(id).recover(recoverInvalidNode[UserNode[F]])
        repositoryNode <- repositoryService.findNode(id).recover(recoverInvalidNode[RepositoryNode[F]])
      } yield List(userNode, repositoryNode).collectFirstSomeM(List(_)).head

  }
  object NodeService {
    def apply[F[_]: Sync](repositories: Repositories[F]): NodeService[F] =
      new NodeService[F](
        UserService[F](repositories.userRepo, repositories.repositoryRepo),
        RepositoryService[F](repositories.repositoryRepo)
      ) {}
  }

  /**
    *
    */
  sealed trait Services[F[_]] {
    def nodeService: NodeService[F]
    def userService: UserService[F]
    def repositoryService: RepositoryService[F]
  }
  object Services {
    private[this] def apply[F[_]: Sync](repos: Repositories[F]) =
      new Services[F] {
        val nodeService: NodeService[F]             = NodeService[F](repos)
        val userService: UserService[F]             = UserService[F](repos.userRepo, repos.repositoryRepo)
        val repositoryService: RepositoryService[F] = RepositoryService[F](repos.repositoryRepo)
      }

    def make[F[_]: Sync](repos: Repositories[F]) =
      Resource.liftF(Sync[F].delay(apply[F](repos)))
  }

}
