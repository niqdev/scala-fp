package com.github.niqdev.caliban
package pagination

import caliban.{ GraphQL, RootResolver }
import cats.effect.Effect
import com.github.niqdev.caliban.pagination.schema._
import com.github.niqdev.caliban.pagination.services._

// https://developer.github.com/v4/explorer
// https://relay.dev/graphql/connections.htm
// https://graphql.org/learn/pagination
// https://graphql.org/learn/global-object-identification
// https://relay.dev/docs/en/graphql-server-specification
// https://medium.com/javascript-in-plain-english/graphql-pagination-using-edges-vs-nodes-in-connections-f2ddb8edffa0
// https://slack.engineering/evolving-api-pagination-at-slack-1c1f644f8e12
object queries {

  final case class NodeArg(id: String)
  final case class UserArg(name: String)
  final case class RepositoryArg(name: String)
  // TODO after|before are cursor base64
  // TODO first (mandatory) with after (optional) OR last (mandatory) with before (optional)
  final case class RepositoriesArg(first: Long, after: String, last: Long, before: String)

  final case class Queries[F[_]](
    node: NodeArg => F[Option[Node]],
    user: UserArg => F[Option[User]],
    repository: RepositoryArg => F[Option[Repository]],
    repositories: RepositoriesArg => F[RepositoryConnection]
  )

  private[this] def nodeQuery[F[_]: Effect](
    nodeService: NodeService[F]
  ): NodeArg => F[Option[Node]] =
    nodeArg => nodeService.findNode(nodeArg.id)

  private[this] def userQuery[F[_]: Effect](
    userService: UserService[F]
  ): UserArg => F[Option[User]] =
    userArg => userService.findByName(userArg.name)

  private[this] def repositoryQuery[F[_]: Effect](
    repositoryService: RepositoryService[F]
  ): RepositoryArg => F[Option[Repository]] =
    repositoryArg => repositoryService.findByName(repositoryArg.name)

  private[this] def repositoryConnectionQuery[F[_]: Effect](
    repositoryService: RepositoryService[F]
  ): RepositoriesArg => F[RepositoryConnection] =
    repositoriesArg => repositoryService.connection(repositoriesArg.first, repositoriesArg.after)

  private[this] def resolver[F[_]: Effect]: Queries[F] = {
    // TODO refactor: move in app
    val repos             = repositories.apply[F]
    val nodeService       = NodeService[F](repos)
    val userService       = UserService[F](repos.userRepo, repos.repositoryRepo)
    val repositoryService = RepositoryService[F](repos.repositoryRepo)

    Queries(
      node = nodeQuery(nodeService),
      user = userQuery(userService),
      repository = repositoryQuery(repositoryService),
      repositories = repositoryConnectionQuery(repositoryService)
    )
  }

  def api[F[_]: Effect]: GraphQL[Any] =
    GraphQL.graphQL(RootResolver(resolver[F]))
}
