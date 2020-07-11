package com.github.niqdev.caliban
package pagination

import com.github.niqdev.caliban.pagination.models._
import com.github.niqdev.caliban.pagination.schema._

object codecs {

  trait SchemaCodec[A, B] {
    def to(a: A): B
  }

  object SchemaCodec {
    def apply[A, B](implicit ev: SchemaCodec[A, B]): SchemaCodec[A, B] = ev

    implicit val userCodec: SchemaCodec[User, UserNode] = ???

    implicit val repositoryCodec: SchemaCodec[Repository, RepositoryNode] = ???
  }

}
