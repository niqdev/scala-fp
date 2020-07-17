package com.github.niqdev.caliban
package pagination

import java.util.UUID

import com.github.niqdev.caliban.pagination.models._
import com.github.niqdev.caliban.pagination.repositories.RowNumber
import com.github.niqdev.caliban.pagination.schema._
import com.github.niqdev.caliban.pagination.schema.arguments._

import scala.util.Try

object codecs {

  /**
    *
    */
  trait SchemaEncoder[A, B] {
    def from(model: A): B
  }

  object SchemaEncoder {
    def apply[A, B](implicit ev: SchemaEncoder[A, B]): SchemaEncoder[A, B] = ev

    implicit lazy val cursorSchemaEncoder: SchemaEncoder[RowNumber, Cursor] =
      rowNumber =>
        Cursor(Base64String.unsafeFrom(utils.toBase64(s"${Cursor.prefix}${rowNumber.value.value}")))

    implicit lazy val userNodeIdSchemaEncoder: SchemaEncoder[UserId, NodeId] =
      model => NodeId(Base64String.unsafeFrom(utils.toBase64(s"${UserNode.idPrefix}${model.value.toString}")))

    implicit def userNodeSchemaEncoder[F[_]](
      implicit
      uniSchemaEncoder: SchemaEncoder[UserId, NodeId]
    ): SchemaEncoder[(User, ForwardPaginationArg => F[RepositoryConnection[F]]), UserNode[F]] = {
      case (user, getRepositoryConnectionF) =>
        UserNode(
          id = uniSchemaEncoder.from(user.id),
          name = user.name.value,
          createdAt = user.createdAt,
          updatedAt = user.updatedAt,
          repositories = getRepositoryConnectionF
        )
    }

    implicit lazy val repositoryNodeIdSchemaEncoder: SchemaEncoder[RepositoryId, NodeId] =
      model =>
        NodeId(Base64String.unsafeFrom(utils.toBase64(s"${RepositoryNode.idPrefix}${model.value.toString}")))

    implicit def repositoryNodeSchemaEncoder[F[_]](
      implicit rniSchemaEncoder: SchemaEncoder[RepositoryId, NodeId]
    ): SchemaEncoder[Repository, RepositoryNode[F]] =
      model =>
        RepositoryNode(
          id = rniSchemaEncoder.from(model.id),
          name = model.name.value,
          url = model.url.value,
          isFork = model.isFork,
          createdAt = model.createdAt,
          updatedAt = model.updatedAt
        )

    implicit def repositoryEdgeSchemaEncoder[F[_]](
      implicit
      cSchemaEncoder: SchemaEncoder[RowNumber, Cursor],
      //rniSchemaEncoder: SchemaEncoder[RepositoryId, NodeId],
      rnSchemaEncoder: SchemaEncoder[Repository, RepositoryNode[F]]
    ): SchemaEncoder[(RowNumber, Repository), RepositoryEdge[F]] = {
      case (rowNumber, model) =>
        RepositoryEdge(
          cursor = cSchemaEncoder.from(rowNumber),
          node = rnSchemaEncoder.from(model)
        )
    }
  }

  final class SchemaEncoderOps[A](private val model: A) extends AnyVal {
    def encodeFrom[B](implicit schemaEncoder: SchemaEncoder[A, B]): B =
      schemaEncoder.from(model)
  }

  /**
    *
    */
  trait SchemaDecoder[A, B] {
    def to(schema: A): Either[Throwable, B]
  }

  object SchemaDecoder {
    def apply[A, B](implicit ev: SchemaDecoder[A, B]): SchemaDecoder[A, B] = ev

    private[this] def uuidSchemaDecoder(prefix: String): SchemaDecoder[NodeId, UUID] =
      schema => {
        val nodeId       = utils.fromBase64(schema.value.value)
        val errorMessage = s"invalid prefix: expected to start with [$prefix] but found [$nodeId]"
        Either
          .cond(
            nodeId.startsWith(prefix),
            utils.removePrefix(nodeId, prefix),
            new IllegalArgumentException(errorMessage)
          )
          .flatMap(uuidString => Try(UUID.fromString(uuidString)).toEither)
      }

    implicit lazy val userIdSchemaDecoder: SchemaDecoder[NodeId, UserId] =
      schema => uuidSchemaDecoder(UserNode.idPrefix).to(schema).map(UserId.apply)

    implicit lazy val repositoryIdSchemaDecoder: SchemaDecoder[NodeId, RepositoryId] =
      schema => uuidSchemaDecoder(RepositoryNode.idPrefix).to(schema).map(RepositoryId.apply)
  }
}
