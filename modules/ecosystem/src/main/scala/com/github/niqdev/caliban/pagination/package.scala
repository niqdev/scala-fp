package com.github.niqdev.caliban

import com.github.niqdev.caliban.pagination.codecs.SchemaEncoderOps

package object pagination {

  final implicit def schemaEncoderSyntax[A](model: A): SchemaEncoderOps[A] =
    new SchemaEncoderOps[A](model)
}
