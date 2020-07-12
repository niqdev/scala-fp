package com.github.niqdev.caliban
package pagination

import java.util.UUID

import com.github.niqdev.caliban.pagination.models._
import com.github.niqdev.caliban.pagination.schema._

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

    implicit lazy val userSchemaEncoder: SchemaEncoder[User, UserNode] = ???

    implicit lazy val repositoryNodeIdSchemaEncoder: SchemaEncoder[RepositoryId, NodeId] =
      model =>
        NodeId(Base64String.unsafeFrom(utils.toBase64(s"${RepositoryNode.idPrefix}${model.value.toString}")))

    implicit def repositorySchemaEncoder(
      implicit idSchemaEncoder: SchemaEncoder[RepositoryId, NodeId]
    ): SchemaEncoder[Repository, RepositoryNode] =
      model =>
        RepositoryNode(
          id = idSchemaEncoder.from(model.id),
          name = model.name.value,
          url = model.url.value,
          isFork = model.isFork,
          createdAt = model.createdAt,
          updatedAt = model.updatedAt
        )
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
            throw new IllegalArgumentException(errorMessage)
          )
          .flatMap(uuidString => Try(UUID.fromString(uuidString)).toEither)
      }

    implicit lazy val userIdSchemaDecoder: SchemaDecoder[NodeId, UserId] =
      schema => uuidSchemaDecoder(UserNode.idPrefix).to(schema).map(UserId.apply)

    implicit lazy val repositoryIdSchemaDecoder: SchemaDecoder[NodeId, RepositoryId] =
      schema => uuidSchemaDecoder(RepositoryNode.idPrefix).to(schema).map(RepositoryId.apply)
  }
}
