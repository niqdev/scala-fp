package com.github.niqdev.caliban
package pagination

import java.time.Instant

import caliban.CalibanError
import caliban.Value.{ IntValue, StringValue }
import caliban.interop.cats.CatsInterop
import caliban.schema.Annotations.GQLInterface
import caliban.schema.{ ArgBuilder, Schema }
import cats.effect.Effect
import cats.syntax.either._
import com.github.niqdev.caliban.pagination.types._
import eu.timepit.refined.string.Url
import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString

object schema extends CommonSchema with CommonArgBuilder {

  @GQLInterface
  sealed trait Node {
    def id: NodeId
  }

  final case class UserNode(
    id: NodeId,
    name: NonEmptyString,
    createdAt: Instant,
    updatedAt: Instant,
    //repository: Repository,
    repositories: RepositoryConnection
  ) extends Node

  // TODO add issue|issues
  final case class RepositoryNode(
    id: NodeId,
    name: NonEmptyString,
    url: Url,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  ) extends Node

  final case class RepositoryConnection(
    edges: List[RepositoryEdge],
    nodes: List[RepositoryNode],
    pageInfo: PageInfo,
    totalCount: NonNegInt
  )

  final case class RepositoryEdge(
    cursor: Cursor,
    node: RepositoryNode
  )

  final case class PageInfo(
    hasNextPage: Boolean,
    hasPreviousPage: Boolean,
    startCursor: Cursor,
    endCursor: Cursor
  )
}

protected[caliban] sealed trait CommonSchema {

  // see caliban.interop.cats.implicits.effectSchema
  implicit def effectSchema[F[_]: Effect, R, A](implicit ev: Schema[R, A]): Schema[R, F[A]] =
    CatsInterop.schema

  implicit val instantSchema: Schema[Any, Instant] =
    Schema.longSchema.contramap(_.getEpochSecond)

  implicit val nonEmptyStringSchema: Schema[Any, NonEmptyString] =
    Schema.stringSchema.contramap(_.value)

  implicit val nodeIdSchema: Schema[Any, NodeId] =
    nonEmptyStringSchema.contramap(_.base64)

  implicit val cursorSchema: Schema[Any, Cursor] =
    nonEmptyStringSchema.contramap(_.base64)

  implicit val offsetSchema: Schema[Any, Offset] =
    Schema.intSchema.contramap(_.nonNegInt.value)
}

protected[caliban] sealed trait CommonArgBuilder {

  implicit val nonEmptyStringArgBuilder: ArgBuilder[NonEmptyString] = {
    case StringValue(value) =>
      NonEmptyString.from(value).leftMap(CalibanError.ExecutionError(_))
    case other =>
      Left(CalibanError.ExecutionError(s"Can't build a NonEmptyString from input $other"))
  }

  implicit val nodeIdArgBuilder: ArgBuilder[NodeId] =
    nonEmptyStringArgBuilder.map(NodeId.apply)

  implicit val cursorArgBuilder: ArgBuilder[Cursor] =
    nonEmptyStringArgBuilder.map(Cursor.apply)

  implicit val offsetArgBuilder: ArgBuilder[Offset] = {
    case value: IntValue =>
      NonNegInt.from(value.toInt).map(Offset.apply).leftMap(CalibanError.ExecutionError(_))
    case other =>
      Left(CalibanError.ExecutionError(s"Can't build a NonNegInt from input $other"))
  }

}
