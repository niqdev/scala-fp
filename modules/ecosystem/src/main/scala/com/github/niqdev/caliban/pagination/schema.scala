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
import com.github.niqdev.caliban.pagination.schema._
import eu.timepit.refined.W
import eu.timepit.refined.api.{ Refined, RefinedTypeOps }
import eu.timepit.refined.string.{ MatchesRegex, Url }
import eu.timepit.refined.types.numeric.NonNegLong
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

object schema extends CommonSchema with CommonArgBuilder {

  // https://stackoverflow.com/questions/475074/regex-to-parse-or-validate-base64-data
  final val Base64Regex = W(
    """^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})$"""
  )
  final type Base64String = String Refined MatchesRegex[Base64Regex.T]
  final object Base64String extends RefinedTypeOps[Base64String, String]

  @newtype case class Offset(value: NonNegLong)
  @newtype case class NodeId(value: Base64String)
  @newtype case class Cursor(value: Base64String)
  object Cursor {
    final val prefix = "cursor:v1:"
  }

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
  object UserNode {
    final val idPrefix = "user:v1:"
  }

  // TODO add issue|issues
  final case class RepositoryNode(
    id: NodeId,
    name: NonEmptyString,
    url: String Refined Url,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  ) extends Node
  object RepositoryNode {
    val idPrefix = "repository:v1:"
  }

  final case class RepositoryConnection(
    edges: List[RepositoryEdge],
    nodes: List[RepositoryNode],
    pageInfo: PageInfo,
    totalCount: NonNegLong
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

  implicit val base64StringSchema: Schema[Any, Base64String] =
    Schema.stringSchema.contramap(_.value)

  implicit val nodeIdSchema: Schema[Any, NodeId] =
    base64StringSchema.contramap(_.value)

  implicit val cursorSchema: Schema[Any, Cursor] =
    base64StringSchema.contramap(_.value)

  implicit val urlSchema: Schema[Any, Url] =
    Schema.stringSchema.contramap(_.toString)

  implicit val offsetSchema: Schema[Any, Offset] =
    Schema.longSchema.contramap(_.value.value)
}

protected[caliban] sealed trait CommonArgBuilder {

  implicit val nonEmptyStringArgBuilder: ArgBuilder[NonEmptyString] = {
    case StringValue(value) =>
      NonEmptyString.from(value).leftMap(CalibanError.ExecutionError(_))
    case other =>
      Left(CalibanError.ExecutionError(s"Can't build a NonEmptyString from input $other"))
  }

  implicit val base64StringArgBuilder: ArgBuilder[Base64String] = {
    case StringValue(value) =>
      Base64String.from(value).leftMap(CalibanError.ExecutionError(_))
    case other =>
      Left(CalibanError.ExecutionError(s"Can't build a Base64String from input $other"))
  }

  implicit val nodeIdArgBuilder: ArgBuilder[NodeId] =
    base64StringArgBuilder.map(NodeId.apply)

  implicit val cursorArgBuilder: ArgBuilder[Cursor] =
    base64StringArgBuilder.map(Cursor.apply)

  implicit val offsetArgBuilder: ArgBuilder[Offset] = {
    case value: IntValue =>
      NonNegLong.from(value.toLong).map(Offset.apply).leftMap(CalibanError.ExecutionError(_))
    case other =>
      Left(CalibanError.ExecutionError(s"Can't build a NonNegInt from input $other"))
  }

}
