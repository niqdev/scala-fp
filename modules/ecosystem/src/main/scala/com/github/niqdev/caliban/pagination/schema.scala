package com.github.niqdev.caliban
package pagination

import java.time.Instant

import caliban.interop.cats.CatsInterop
import caliban.schema.Annotations.GQLInterface
import caliban.schema.Schema
import cats.effect.Effect
import com.github.niqdev.caliban.pagination.models._

// TODO newtype + refined
object schema extends CommonSchema {

  @GQLInterface
  sealed trait Node {
    def id: String
  }

  @GQLInterface
  sealed trait Base {
    def createdAt: Instant
    def updatedAt: Instant
  }

  // TODO mapN ?
  final case class UserNode(
    id: String,
    name: String,
    createdAt: Instant,
    updatedAt: Instant,
    //repository: Repository, TODO findByName
    repositories: RepositoryConnection
  ) extends Node
      with Base
  object UserNode {
    val idPrefix = "user:v1:"

    def fromUser(repositories: RepositoryConnection): User => UserNode =
      user =>
        UserNode(
          id = utils.toBase64(s"$idPrefix${user.id}"),
          name = user.name,
          createdAt = user.createdAt,
          updatedAt = user.updatedAt,
          repositories
        )
  }

  // TODO add issue|issues
  final case class RepositoryNode(
    id: String,
    name: String,
    url: String,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  ) extends Node
      with Base
  object RepositoryNode {
    val idPrefix = "repository:v1:"

    val fromRepository: Repository => RepositoryNode =
      repository =>
        RepositoryNode(
          utils.toBase64(s"$idPrefix${repository.id}"),
          repository.name,
          repository.url,
          repository.isFork,
          repository.createdAt,
          repository.updatedAt
        )
  }

  final case class RepositoryConnection(
    edges: List[RepositoryEdge],
    nodes: List[RepositoryNode],
    pageInfo: PageInfo,
    totalCount: Long
  )

  final case class RepositoryEdge(
    cursor: String,
    node: RepositoryNode
  )
  object RepositoryEdge {
    val cursorPrefix = "cursor:v1:"

    val fromRepository: Repository => RepositoryEdge =
      repository =>
        RepositoryEdge(
          utils.toBase64(s"$cursorPrefix${RepositoryNode.idPrefix}${repository.id}"),
          RepositoryNode.fromRepository(repository)
        )
  }

  final case class PageInfo(
    hasNextPage: Boolean,
    hasPreviousPage: Boolean,
    startCursor: String,
    endCursor: String
  )
}

protected[caliban] sealed trait CommonSchema {

  // caliban.interop.cats.implicits.effectSchema
  implicit def effectSchema[F[_]: Effect, R, A](implicit ev: Schema[R, A]): Schema[R, F[A]] =
    CatsInterop.schema

  implicit val instantSchema: Schema[Any, Instant] =
    Schema.longSchema.contramap(_.getEpochSecond)
}
