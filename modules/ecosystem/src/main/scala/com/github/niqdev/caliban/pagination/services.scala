package com.github.niqdev.caliban
package pagination

import cats.effect.{ Resource, Sync }
import com.github.niqdev.caliban.pagination.repositories._
import com.github.niqdev.caliban.pagination.schema._
import eu.timepit.refined.types.string.NonEmptyString

/*
import cats.instances.list._
import cats.instances.option._
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.foldable._
import cats.syntax.functor._
import cats.syntax.nested._
 */

// TODO
@scala.annotation.nowarn
object services {

  /*
  private[this] val decodeNodeId: String => scala.util.Try[Long] =
    value => utils.longFromBase64(value, UserNode.idPrefix, RepositoryNode.idPrefix)
   */

  /**
    *
    */
  sealed abstract class UserService[F[_]](
    userRepo: UserRepo[F],
    repositoryRepo: RepositoryRepo[F]
  )(
    implicit F: Sync[F]
  ) {

    def findNode(id: NodeId): F[Option[UserNode]]             = ???
    def findByName(name: NonEmptyString): F[Option[UserNode]] = ???

    /*
    private[this] def findRepositoryConnection(userId: Long): F[RepositoryConnection] =
      for {
        // TODO use + return paging info
        repositories <- repositoryRepo.findAllByUserId(userId)
        edges        <- F.pure(repositories.map(RepositoryEdge.fromRepository))
        nodes        <- F.pure(repositories.map(RepositoryNode.fromRepository))
        pageInfo <- F.pure {
          // TODO https://relay.dev/graphql/connections.htm
          PageInfo(true, true, "startCursor", "endCursor")
        }
        totalCount <- repositoryRepo.countByUserId(userId)
      } yield RepositoryConnection(edges, nodes, pageInfo, totalCount)

    def findNode(id: String): F[Option[UserNode]] =
      for {
        userId               <- F.fromTry(decodeNodeId(id))
        repositoryConnection <- findRepositoryConnection(userId)
        maybeUserNode        <- userRepo.findById(userId).nested.map(UserNode.fromUser(repositoryConnection)).value
      } yield maybeUserNode

    def findByName(name: String): F[Option[UserNode]] =
      (for {
        maybeUser            <- userRepo.findByName(name)
        user                 <- F.fromOption(maybeUser, new IllegalArgumentException("invalid name"))
        repositoryConnection <- findRepositoryConnection(user.id)
        userNode             <- F.pure(user).map(UserNode.fromUser(repositoryConnection))
      } yield userNode).redeem(_ => None, Some(_))

   */

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

    def findNode(id: NodeId): F[Option[RepositoryNode]]             = ???
    def findByName(name: NonEmptyString): F[Option[RepositoryNode]] = ???
    def connection(
      first: Option[Offset],
      after: Option[Cursor],
      last: Option[Offset],
      before: Option[Cursor]
    ): F[RepositoryConnection] = ???

    /*
    def findNode(id: String): F[Option[RepositoryNode]] =
      for {
        repositoryId <- F.fromTry(decodeNodeId(id))
        maybeRepositoryNode <- repositoryRepo
          .findById(repositoryId)
          .nested
          .map(RepositoryNode.fromRepository)
          .value
      } yield maybeRepositoryNode

    def findByName(name: String): F[Option[RepositoryNode]] =
      repositoryRepo.findByName(name).nested.map(RepositoryNode.fromRepository).value

   */

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

    def findNode(id: NodeId): F[Option[Node]] = ???

    /*
    // TODO bug: returns always user (same id)
    // TODO invoke service based on prefix starts with
    def findNode(id: String): F[Option[Node]] =
      for {
        userNode       <- userService.findNode(id)
        repositoryNode <- repositoryService.findNode(id)
      } yield List(userNode, repositoryNode).foldK

   */
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
