package com.github.niqdev.caliban
package pagination

import caliban.{ GraphQL, RootResolver }
import cats.effect.Effect
import com.github.niqdev.caliban.pagination.repositories._
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
  // TODO after is cursor/id base64
  final case class RepositoriesArg(first: Long, after: String)

  final case class Queries[F[_]](
    node: NodeArg => F[Option[Node]],
    user: UserArg => F[Option[User]],
    repository: RepositoryArg => F[Option[Repository]],
    repositories: RepositoriesArg => F[RepositoryConnection]
  )

  private[this] def nodeQuery[F[_]: Effect](
    repositories: Repositories[F]
  ): NodeArg => F[Option[Node]] =
    nodeArg => NodeService[F](repositories).findNode(nodeArg.id)

  private[this] def userQuery[F[_]: Effect](
    repository: UserRepo[F]
  ): UserArg => F[Option[User]] =
    userArg => UserService[F](repository).findByName(userArg.name)

  private[this] def repositoryQuery[F[_]: Effect](
    repository: RepositoryRepo[F]
  ): RepositoryArg => F[Option[Repository]] =
    repositoryArg => RepositoryService[F](repository).findByName(repositoryArg.name)

  private[this] def repositoryConnectionQuery[F[_]: Effect](
    repositories: Repositories[F]
  ): RepositoriesArg => F[RepositoryConnection] =
    repositoriesArg =>
      RepositoryService[F](repositories.repositoryRepo)
        .connection(repositoriesArg.first, repositoriesArg.after)

  private[this] def resolver[F[_]: Effect]: Queries[F] = {
    // TODO refactor: move in app
    val repos = repositories.apply[F]

    Queries(
      node = nodeQuery(repos),
      user = userQuery(repos.userRepo),
      repository = repositoryQuery(repos.repositoryRepo),
      repositories = repositoryConnectionQuery(repos)
    )
  }

  def api[F[_]: Effect]: GraphQL[Any] =
    GraphQL.graphQL(RootResolver(resolver[F]))
}
