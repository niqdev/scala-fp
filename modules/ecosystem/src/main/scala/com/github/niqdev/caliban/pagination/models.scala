package com.github.niqdev.caliban
package pagination

import java.time.Instant

// TODO newtype + refined
object models {

  // TODO
  trait Model {
    def id: String
  }

  final case class UserModel(
    id: String,
    name: String,
    createdAt: Instant,
    updatedAt: Instant
  ) extends Model

  final case class RepositoryModel(
    id: String,
    userId: String,
    url: String,
    name: String,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  ) extends Model

  val users: List[UserModel] = List(
    UserModel(
      id = "userId",
      name = "userName",
      createdAt = Instant.now,
      updatedAt = Instant.now
    )
  )

  val repositories: List[RepositoryModel] = List(
    RepositoryModel(
      id = "repositoryId1",
      userId = "userId",
      name = "repositoryName1",
      url = "repositoryUrl1",
      isFork = false,
      createdAt = Instant.now,
      updatedAt = Instant.now
    ),
    RepositoryModel(
      id = "repositoryId2",
      userId = "userIdNew",
      name = "repositoryName2",
      url = "repositoryUrl2",
      isFork = true,
      createdAt = Instant.now,
      updatedAt = Instant.now
    )
  )

}
