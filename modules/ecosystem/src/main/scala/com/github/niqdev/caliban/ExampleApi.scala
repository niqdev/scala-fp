package com.github.niqdev.caliban

import caliban.CalibanError.ExecutionError
import caliban.Value.StringValue
import caliban.schema.{ ArgBuilder, Schema }
import caliban.{ GraphQL, RootResolver }
import cats.syntax.either.catsSyntaxEither
import eu.timepit.refined.types.string.NonEmptyString

object ExampleApi {

  implicit val nonEmptyStringSchema: Schema[Any, NonEmptyString] =
    Schema.stringSchema.contramap(_.value)

  implicit val string: ArgBuilder[NonEmptyString] = {
    case StringValue(value) =>
      NonEmptyString.from(value).leftMap(msg => ExecutionError(msg))
    case other =>
      Left(ExecutionError(s"Can't build a NonEmptyString from input $other"))
  }

  // schema
  case class ModelId(id: NonEmptyString)
  case class Queries(
    models: List[ExampleModel],
    model: ModelId => Option[ExampleModel]
  )

  // resolver
  private[this] val queries = Queries(
    ExampleService.getModels,
    args => ExampleService.getModel(args.id)
  )

  val api = GraphQL.graphQL(RootResolver(queries))
}
