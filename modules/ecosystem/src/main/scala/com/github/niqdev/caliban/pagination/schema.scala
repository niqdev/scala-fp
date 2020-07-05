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
  final case class User(
    id: String,
    name: String,
    createdAt: Instant,
    updatedAt: Instant,
    //repository: Repository, TODO findByName
    repositories: RepositoryConnection
  ) extends Node
      with Base
  object User {
    val idPrefix = "user:v1:"

    def fromModel(repositories: RepositoryConnection): UserModel => User =
      model =>
        User(
          id = utils.toBase64(s"$idPrefix${model.id}"),
          name = model.name,
          createdAt = model.createdAt,
          updatedAt = model.updatedAt,
          repositories
        )
  }

  // TODO add issue|issues
  final case class Repository(
    id: String,
    name: String,
    url: String,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  ) extends Node
      with Base
  object Repository {
    val idPrefix = "repository:v1:"

    val fromModel: RepositoryModel => Repository =
      model =>
        Repository(
          utils.toBase64(s"$idPrefix${model.id}"),
          model.name,
          model.url,
          model.isFork,
          model.createdAt,
          model.updatedAt
        )
  }

  final case class RepositoryConnection(
    edges: List[RepositoryEdge],
    nodes: List[Repository],
    pageInfo: PageInfo,
    totalCount: Long
  )

  final case class RepositoryEdge(
    cursor: String,
    node: Repository
  )
  object RepositoryEdge {
    val cursorPrefix = "cursor:v1:"

    val fromRepositoryModel: RepositoryModel => RepositoryEdge =
      repositoryModel =>
        RepositoryEdge(
          utils.toBase64(s"$cursorPrefix${Repository.idPrefix}${repositoryModel.id}"),
          Repository.fromModel(repositoryModel)
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
