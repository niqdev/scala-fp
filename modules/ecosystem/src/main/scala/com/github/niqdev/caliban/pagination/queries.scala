package com.github.niqdev.caliban
package pagination

import caliban.{ GraphQL, RootResolver }
import cats.effect.Effect
import com.github.niqdev.caliban.pagination.schema._
import com.github.niqdev.caliban.pagination.schema.arguments._
import com.github.niqdev.caliban.pagination.services._

// https://developer.github.com/v4/explorer
// https://relay.dev/graphql/connections.htm
// https://graphql.org/learn/pagination
// https://graphql.org/learn/global-object-identification
// https://relay.dev/docs/en/graphql-server-specification
// https://medium.com/javascript-in-plain-english/graphql-pagination-using-edges-vs-nodes-in-connections-f2ddb8edffa0
// https://slack.engineering/evolving-api-pagination-at-slack-1c1f644f8e12
object queries {

  /**
    * Root Nodes
    */
  final case class Queries[F[_]](
    node: NodeArg => F[Option[Node[F]]],
    user: UserArg => F[Option[UserNode[F]]],
    repository: RepositoryArg => F[Option[RepositoryNode[F]]],
    repositories: ForwardPaginationArg => F[RepositoryConnection[F]]
  )
  object Queries {
    private[this] def resolver[F[_]: Effect](services: Services[F]): Queries[F] =
      Queries(
        node = nodeArg => services.nodeService.findNode(nodeArg.id),
        user = userArg => services.userService.findByName(userArg.name),
        repository = repositoryArg => services.repositoryService.findByName(repositoryArg.name),
        repositories = services.repositoryService.connection(None)
      )

    // TODO log errors: mapError or Wrapper
    def api[F[_]: Effect](services: Services[F]): GraphQL[Any] =
      GraphQL.graphQL(RootResolver(resolver[F](services)))
  }
}

/*

query findRepositoryByName {
  repository(name: "zio") {
    id
    name
    url
    isFork
    createdAt
    updatedAt
  }
}

query getNodeById {
  node(id: "opaqueCursor") {
    id
    ... on UserNodeF {
      id
      name
      createdAt
      updatedAt
    }
    ... on RepositoryNodeF {
      name
      url
      isFork
      createdAt
      updatedAt
    }
  }
}

query getSimpleUser {
  user(name: "typelevel") {
    id
    name
    repositories(first: 10, after: "opaqueCursor") {
      edges {
        cursor
        node {
          id
          name
        }
      }
      pageInfo {
        hasNextPage
      }
    }
  }
}

query getRepositories {
  repositories(first: 2, after: "opaqueCursor") {
    nodes {
      id
      name
    }
  }
}

query getUser {
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

 */
