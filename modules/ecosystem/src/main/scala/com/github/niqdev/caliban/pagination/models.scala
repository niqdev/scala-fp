package com.github.niqdev.caliban
package pagination

import java.time.Instant
import java.util.UUID

import doobie.util.meta.Meta
import eu.timepit.refined.string.Url
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

object models {

  @newtype
  case class UserId(value: UUID)
  object UserId {
    implicit val userIdMeta: Meta[UserId] =
      Meta.Advanced.other[UUID]("id").timap(UserId(_))(_.value)
  }

  @newtype
  case class RepositoryId(value: java.util.UUID)
  object RepositoryId {
    implicit val repositoryIdMeta: Meta[RepositoryId] =
      Meta.Advanced.other[UUID]("id").timap(RepositoryId(_))(_.value)
  }

  final case class User(
    id: UserId,
    name: NonEmptyString,
    createdAt: Instant,
    updatedAt: Instant
  )

  final case class Repository(
    id: RepositoryId,
    userId: UserId,
    name: NonEmptyString,
    url: Url,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  )

}
