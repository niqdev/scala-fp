package com.github.niqdev.caliban

import eu.timepit.refined.types.string.NonEmptyString

object ExampleService {

  def getModels: List[ExampleModel] =
    ExampleModel.models

  def getModel(id: NonEmptyString): Option[ExampleModel] =
    ExampleModel.models.find(_.id == id)
}
