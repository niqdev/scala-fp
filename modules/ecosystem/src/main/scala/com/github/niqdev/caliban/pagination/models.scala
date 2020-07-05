package com.github.niqdev.caliban
package pagination

import java.time.Instant

// TODO newtype + refined
// TODO mock db: https://github.com/typelevel and https://github.com/zio
object models {

  final case class UserModel(
    id: Long,
    name: String,
    createdAt: Instant,
    updatedAt: Instant
  )

  final case class RepositoryModel(
    id: Long,
    userId: Long,
    url: String,
    name: String,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  )

  val users: List[UserModel] = List(
    UserModel(
      id = 1,
      name = "userName1",
      createdAt = Instant.now,
      updatedAt = Instant.now
    ),
    UserModel(
      id = 2,
      name = "userName2",
      createdAt = Instant.now,
      updatedAt = Instant.now
    )
  )

  val repositories: List[RepositoryModel] = List(
    RepositoryModel(
      id = 1,
      userId = 1,
      name = "repositoryName1",
      url = "repositoryUrl1",
      isFork = false,
      createdAt = Instant.now,
      updatedAt = Instant.now
    ),
    RepositoryModel(
      id = 2,
      userId = 1,
      name = "repositoryName2",
      url = "repositoryUrl2",
      isFork = true,
      createdAt = Instant.now,
      updatedAt = Instant.now
    ),
    RepositoryModel(
      id = 3,
      userId = 2,
      name = "repositoryName3",
      url = "repositoryUrl3",
      isFork = false,
      createdAt = Instant.now,
      updatedAt = Instant.now
    )
  )

}
