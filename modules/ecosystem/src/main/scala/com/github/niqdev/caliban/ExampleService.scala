package com.github.niqdev.caliban

import eu.timepit.refined.types.string.NonEmptyString

object ExampleService {
  def getModels: List[ExampleModel]                      = List.empty[ExampleModel]
  def getModel(id: NonEmptyString): Option[ExampleModel] = None
}
