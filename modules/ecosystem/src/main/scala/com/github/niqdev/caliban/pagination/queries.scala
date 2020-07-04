package com.github.niqdev.caliban
package pagination

import caliban.{ GraphQL, RootResolver }
import cats.effect.Effect
import com.github.niqdev.caliban.pagination.schema._
import com.github.niqdev.caliban.pagination.services.NodeService

// https://developer.github.com/v4/explorer
// https://relay.dev/graphql/connections.htm
// https://graphql.org/learn/pagination
// https://graphql.org/learn/global-object-identification
// https://relay.dev/docs/en/graphql-server-specification
// https://medium.com/javascript-in-plain-english/graphql-pagination-using-edges-vs-nodes-in-connections-f2ddb8edffa0
// https://slack.engineering/evolving-api-pagination-at-slack-1c1f644f8e12
object queries {

  final case class NodeArg(id: String)
  final case class Queries[F[_]](
    node: NodeArg => F[Option[Node]]
  )

  private[this] def resolver[F[_]: Effect]: Queries[F] =
    Queries(nodeArg => NodeService.init[F].findNode(nodeArg.id))

  def api[F[_]: Effect]: GraphQL[Any] =
    GraphQL.graphQL(RootResolver(resolver[F]))
}
