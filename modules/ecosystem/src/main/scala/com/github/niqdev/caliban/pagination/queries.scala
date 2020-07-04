package com.github.niqdev.caliban
package pagination

import java.time.Instant

import caliban.schema.Schema
import caliban.{GraphQL, RootResolver}
import cats.effect.Effect
import com.github.niqdev.caliban.pagination.models._
import com.github.niqdev.caliban.pagination.services.NodeService

object queries {

  import caliban.interop.cats.implicits.effectSchema

  implicit val instantSchema: Schema[Any, Instant] =
    Schema.longSchema.contramap(_.getEpochSecond)

  final case class NodeArg(id: String)
  final case class Queries[F[_]](
    node: NodeArg => F[Option[Node]]
  )

  private[this] def resolver[F[_]: Effect]: Queries[F] =
    Queries(nodeArg => NodeService[F].findById(nodeArg.id))

  def api[F[_]: Effect]: GraphQL[Any] =
    GraphQL.graphQL(RootResolver(resolver[F]))
}
