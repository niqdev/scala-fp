package com.github.niqdev.caliban
package pagination

import caliban.{ GraphQL, RootResolver }
import cats.effect.Effect
import com.github.niqdev.caliban.pagination.queries.arguments._
import com.github.niqdev.caliban.pagination.schema._
import com.github.niqdev.caliban.pagination.services._
import eu.timepit.refined.types.string.NonEmptyString

// https://developer.github.com/v4/explorer
// https://relay.dev/graphql/connections.htm
// https://graphql.org/learn/pagination
// https://graphql.org/learn/global-object-identification
// https://relay.dev/docs/en/graphql-server-specification
// https://medium.com/javascript-in-plain-english/graphql-pagination-using-edges-vs-nodes-in-connections-f2ddb8edffa0
// https://slack.engineering/evolving-api-pagination-at-slack-1c1f644f8e12
object queries {

  object arguments {
    final case class NodeArg(id: NodeId)
    final case class UserArg(name: NonEmptyString)
    final case class RepositoryArg(name: NonEmptyString)
    // TODO after|before are cursor base64
    // TODO first (mandatory) with after (optional) OR last (mandatory) with before (optional)
    final case class RepositoriesArg(
      first: Option[Offset],
      after: Option[Cursor],
      last: Option[Offset],
      before: Option[Cursor]
    )
  }

  /**
    *
    */
  final case class Queries[F[_]](
    node: NodeArg => F[Option[Node]],
    user: UserArg => F[Option[UserNode]],
    repository: RepositoryArg => F[Option[RepositoryNode]],
    repositories: RepositoriesArg => F[RepositoryConnection]
  )
  object Queries extends NodeQueries with UserQueries with RepositoryQueries {
    private[this] def resolver[F[_]: Effect](services: Services[F]): Queries[F] =
      Queries(
        node = Queries.nodeQuery(services.nodeService),
        user = Queries.userQuery(services.userService),
        repository = Queries.repositoryQuery(services.repositoryService),
        repositories = Queries.repositoryConnectionQuery(services.repositoryService)
      )

    def api[F[_]: Effect](services: Services[F]): GraphQL[Any] =
      GraphQL.graphQL(RootResolver(resolver[F](services)))
  }

}

private[pagination] sealed trait NodeQueries {
  def nodeQuery[F[_]: Effect](
    nodeService: NodeService[F]
  ): NodeArg => F[Option[Node]] =
    arg => nodeService.findNode(arg.id)
}

private[pagination] sealed trait UserQueries {
  def userQuery[F[_]: Effect](
    userService: UserService[F]
  ): UserArg => F[Option[UserNode]] =
    arg => userService.findByName(arg.name)
}

private[pagination] sealed trait RepositoryQueries {
  def repositoryQuery[F[_]: Effect](
    repositoryService: RepositoryService[F]
  ): RepositoryArg => F[Option[RepositoryNode]] =
    arg => repositoryService.findByName(arg.name)

  def repositoryConnectionQuery[F[_]: Effect](
    repositoryService: RepositoryService[F]
  ): RepositoriesArg => F[RepositoryConnection] =
    arg => repositoryService.connection(arg.first, arg.after, arg.last, arg.before)
}

/*

query user {
  user(name: "typelevel") {
    id
    name
    createdAt
    updatedAt
    repositories(first: 10) {
      edges {
        cursor
        node {
          id
          name
          url
          isFork
          createdAt
          updatedAt
        }
      }
      pageInfo {
        hasNextPage
        hasPreviousPage
        startCursor
        endCursor
      }
      totalCount
      nodes {
        id
        name
        url
        isFork
        createdAt
        updatedAt
      }
    }
  }
}

query node {
  getNode: node(id: "opaqueCursor") {
    id
    ... on UserNode {
      name
      createdAt
      updatedAt
    }
    ... on RepositoryNode {
      name
      url
      isFork
      createdAt
      updatedAt
    }
  }
}

 */
