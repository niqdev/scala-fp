package com.github.niqdev.caliban
package pagination

import java.time.Instant
import java.util.UUID

import eu.timepit.refined.string.Url
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

object models {

  @newtype case class UserId(value: UUID)

  @newtype case class RepositoryId(value: UUID)

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
