package com.github.niqdev.caliban
package pagination

import cats.effect.Sync
import cats.instances.list._
import cats.instances.option._
import cats.syntax.flatMap._
import cats.syntax.foldable._
import cats.syntax.functor._
import cats.syntax.nested._
import com.github.niqdev.caliban.pagination.repositories._
import com.github.niqdev.caliban.pagination.schema._

object services {

  sealed trait ServiceI[F[_], T <: Node] {
    def findNode(id: String): F[Option[T]]
  }

  /**
    *
    */
  sealed abstract class UserService[F[_]](userRepo: UserRepo[F])(implicit F: Sync[F])
      extends ServiceI[F, User] {

    override def findNode(id: String): F[Option[User]] =
      userRepo.findById(id).nested.map(User.fromModel).value
  }
  object UserService {
    def apply[F[_]: Sync](userRepo: UserRepo[F]): UserService[F] =
      new UserService[F](userRepo) {}
  }

  /**
    *
    */
  sealed abstract class RepositoryService[F[_]](repositoryRepo: RepositoryRepo[F])(implicit F: Sync[F])
      extends ServiceI[F, Repository] {

    override def findNode(id: String): F[Option[Repository]] =
      repositoryRepo.findById(id).nested.map(Repository.fromModel).value
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
    def init[F[_]: Sync]: NodeService[F] =
      apply[F](repositories.apply[F])

    def apply[F[_]: Sync](repositories: Repositories[F]): NodeService[F] =
      new NodeService[F](
        UserService[F](repositories.userRepo),
        RepositoryService[F](repositories.repositoryRepo)
      ) {}
  }
}
