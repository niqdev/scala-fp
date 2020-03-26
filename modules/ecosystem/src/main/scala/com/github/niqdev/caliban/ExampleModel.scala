package com.github.niqdev.caliban

import eu.timepit.refined.auto.autoRefineV
import eu.timepit.refined.types.string.NonEmptyString

case class ExampleModel(
  id: NonEmptyString,
  description: String,
  count: Int,
  valid: Boolean
)

object ExampleModel {

  val models = List(
    ExampleModel("model-1", "aaa", 1, true),
    ExampleModel("model-2", "bbb", 2, false),
    ExampleModel("model-3", "ccc", 3, true),
    ExampleModel("model-4", "ddd", 4, false),
    ExampleModel("model-5", "eee", 5, true),
    ExampleModel("model-6", "fff", 6, false),
    ExampleModel("model-6", "ggg", 7, true),
    ExampleModel("model-8", "hhh", 8, false)
  )
}
