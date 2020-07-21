package com.github.niqdev.caliban
package pagination

import java.time.Instant
import java.util.UUID

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Url
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

object models {

  @newtype case class UserId(value: UUID)
  @newtype case class UserName(value: NonEmptyString)

  final case class User(
    id: UserId,
    name: UserName,
    createdAt: Instant,
    updatedAt: Instant
  )

  @newtype case class RepositoryId(value: UUID)
  @newtype case class RepositoryName(value: NonEmptyString)
  @newtype case class RepositoryUrl(value: String Refined Url)

  final case class Repository(
    id: RepositoryId,
    userId: UserId,
    name: RepositoryName,
    url: RepositoryUrl,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  )

}
