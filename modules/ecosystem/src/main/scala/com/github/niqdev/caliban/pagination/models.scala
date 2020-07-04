package com.github.niqdev.caliban
package pagination

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

  // createdAt|updatedAt

  // createdAt|email|id|name|repository|repositories|updatedAt
  final case class User(id: String, name: String) extends Node

  // createdAt|id|isFork|isPrivate|issue|issues|name|updatedAt
  final case class Repository(id: String, url: String) extends Node

}
