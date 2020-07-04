package com.github.niqdev.caliban
package pagination

import java.time.Instant

import caliban.interop.cats.CatsInterop
import caliban.schema.Annotations.GQLInterface
import caliban.schema.Schema
import cats.effect.Effect
import com.github.niqdev.caliban.pagination.models._

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
    //repository: Option[Repository],
    //repositories: List[Repository],
    createdAt: Instant,
    updatedAt: Instant
  ) extends Node
      with Base
  object User {
    val fromModel: UserModel => User =
      model =>
        User(
          id = model.id,
          name = model.name,
          createdAt = model.createdAt,
          updatedAt = model.updatedAt
        )
  }

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
    val fromModel: RepositoryModel => Repository =
      model =>
        Repository(
          model.id,
          model.name,
          model.url,
          model.isFork,
          model.createdAt,
          model.updatedAt
        )
  }
}

protected[caliban] sealed trait CommonSchema {

  // caliban.interop.cats.implicits.effectSchema
  implicit def effectSchema[F[_]: Effect, R, A](implicit ev: Schema[R, A]): Schema[R, F[A]] =
    CatsInterop.schema

  implicit val instantSchema: Schema[Any, Instant] =
    Schema.longSchema.contramap(_.getEpochSecond)
}
