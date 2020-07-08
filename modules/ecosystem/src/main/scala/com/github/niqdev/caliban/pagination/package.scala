package com.github.niqdev.caliban

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

// TODO fix warn: it is not recommended to define classes/objects inside of package objects.
package object pagination {

  // TODO MatchesRegex
  final type Base64 = NonEmptyString

  @newtype case class NodeId(base64: Base64)
  @newtype case class Cursor(base64: Base64)
  @newtype case class Offset(nonNegInt: NonNegInt)

}
