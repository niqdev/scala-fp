package com.github.niqdev.caliban

import org.scalatest.{ Matchers, WordSpecLike }

final class ExampleApiSpec extends WordSpecLike with Matchers {

  "ExampleApi" should {

    "verify" in {
      val schema =
        """|schema {
          |  query: Queries
          |}
          |
          |type ExampleModel {
          |  id: String!
          |  description: String!
          |  count: Int!
          |  valid: Boolean!
          |}
          |
          |type Queries {
          |  models: [ExampleModel!]!
          |  model(id: String!): ExampleModel
          |}""".stripMargin

      ExampleApi.api.render shouldBe schema
    }
  }
}
