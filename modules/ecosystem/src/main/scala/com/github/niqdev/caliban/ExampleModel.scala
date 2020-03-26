package com.github.niqdev.caliban

import eu.timepit.refined.types.string.NonEmptyString

case class ExampleModel(
  id: NonEmptyString,
  description: String,
  count: Int,
  valid: Boolean
)
