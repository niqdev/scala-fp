package com.github.niqdev.caliban
package pagination

import java.time.Instant

// TODO newtype + refined
object models {

  final case class User(
    id: Long,
    name: String,
    createdAt: Instant,
    updatedAt: Instant
  )

  final case class Repository(
    id: Long,
    userId: Long,
    name: String,
    url: String,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  )

  val users: List[User] = List(
    User(
      id = 1,
      name = "userName1",
      createdAt = Instant.now,
      updatedAt = Instant.now
    ),
    User(
      id = 2,
      name = "userName2",
      createdAt = Instant.now,
      updatedAt = Instant.now
    )
  )

  val repositories: List[Repository] = List(
    Repository(
      id = 1,
      userId = 1,
      name = "repositoryName1",
      url = "repositoryUrl1",
      isFork = false,
      createdAt = Instant.now,
      updatedAt = Instant.now
    ),
    Repository(
      id = 2,
      userId = 1,
      name = "repositoryName2",
      url = "repositoryUrl2",
      isFork = true,
      createdAt = Instant.now,
      updatedAt = Instant.now
    ),
    Repository(
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
