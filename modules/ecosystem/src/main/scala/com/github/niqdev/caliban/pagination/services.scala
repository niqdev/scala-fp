package com.github.niqdev.caliban.pagination

import cats.effect.Sync
import cats.instances.list._
import cats.instances.option._
import cats.syntax.flatMap._
import cats.syntax.foldable._
import cats.syntax.functor._
import com.github.niqdev.caliban.pagination.models._

object services {

  abstract class UserService[F[_]](implicit F: Sync[F]) {
    def findById(id: String): F[Option[Node]] =
      F.pure(if (id == "userId") Some(User(id, "userName")) else None)
  }
  object UserService {
    def apply[F[_]: Sync]: UserService[F] =
      new UserService[F]() {}
  }

  abstract class RepositoryService[F[_]](implicit F: Sync[F]) {
    def findById(id: String): F[Option[Node]] =
      F.pure(if (id == "repositoryId") Some(Repository(id, "repositoryName")) else None)
  }
  object RepositoryService {
    def apply[F[_]: Sync]: RepositoryService[F] =
      new RepositoryService[F]() {}
  }

  abstract class NodeService[F[_]: Sync](
    userService: UserService[F],
    repositoryService: RepositoryService[F]
  ) {
    def findById(id: String): F[Option[Node]] =
      for {
        user       <- userService.findById(id)
        repository <- repositoryService.findById(id)
      } yield List(user, repository).foldK
  }
  object NodeService {
    def apply[F[_]: Sync]: NodeService[F] =
      new NodeService[F](UserService[F], RepositoryService[F]) {}
  }
}
