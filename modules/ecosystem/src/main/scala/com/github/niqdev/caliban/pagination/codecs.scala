package com.github.niqdev.caliban
package pagination

import com.github.niqdev.caliban.pagination.models._
import com.github.niqdev.caliban.pagination.schema._
import eu.timepit.refined.types.string.NonEmptyString

object codecs {

  trait SchemaDecoder[A, B] {
    def to(a: A): B
  }

  object SchemaDecoder {
    def apply[A, B](implicit ev: SchemaDecoder[A, B]): SchemaDecoder[A, B] = ev

    implicit lazy val userSchemaDecoder: SchemaDecoder[User, UserNode] = ???

    // TODO id base64
    implicit lazy val repositorySchemaDecoder: SchemaDecoder[Repository, RepositoryNode] =
      repository =>
        RepositoryNode(
          id = NodeId(NonEmptyString.unsafeFrom(repository.id.value.toString)),
          name = repository.name.value,
          url = repository.url.value,
          isFork = repository.isFork,
          createdAt = repository.createdAt,
          updatedAt = repository.updatedAt
        )
  }

}
