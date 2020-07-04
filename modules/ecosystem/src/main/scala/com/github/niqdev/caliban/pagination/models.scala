package com.github.niqdev.caliban
package pagination

import java.time.Instant

import caliban.schema.Annotations.GQLInterface

// https://developer.github.com/v4/explorer
// https://relay.dev/graphql/connections.htm
// https://graphql.org/learn/pagination
// https://graphql.org/learn/global-object-identification
// https://relay.dev/docs/en/graphql-server-specification
// https://medium.com/javascript-in-plain-english/graphql-pagination-using-edges-vs-nodes-in-connections-f2ddb8edffa0
// https://slack.engineering/evolving-api-pagination-at-slack-1c1f644f8e12
object models {

  @GQLInterface
  sealed trait Node {
    def id: String
  }

  @GQLInterface
  sealed trait Base {
    def createdAt: Instant
    def updatedAt: Instant
  }

  // repository
  final case class User(
    id: String,
    name: String,
    repository: Repository,
    // TODO
    repositories: List[Repository],
    createdAt: Instant,
    updatedAt: Instant
  ) extends Node
      with Base

  final case class Repository(
    id: String,
    name: String,
    url: String,
    isFork: Boolean,
    createdAt: Instant,
    updatedAt: Instant
  ) extends Node
      with Base

  val repositories: List[Repository] = List(
    Repository(
      id = "userId",
      name = "myRepository",
      url = "myUrl",
      isFork = false,
      createdAt = Instant.now,
      updatedAt = Instant.now
    )
  )

  val users: List[User] = List(
    User(
      id = "repositoryId",
      name = "repositoryName",
      repository = repositories.head,
      repositories = repositories,
      createdAt = Instant.now,
      updatedAt = Instant.now
    )
  )

}
