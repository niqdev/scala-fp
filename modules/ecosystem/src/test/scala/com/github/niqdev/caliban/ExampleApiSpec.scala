package com.github.niqdev.caliban

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class ExampleApiSpec extends AnyWordSpecLike with Matchers {

  "ExampleApi" should {

    "verify schema" in {
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

    "verify query" in {
      val query =
        """
          |{
          |  models {
          |    id
          |  }
          |  model(id: "model-8") {
          |    id
          |    description
          |    count
          |    valid
          |  }
          |}
          |""".stripMargin

      @com.github.ghik.silencer.silent
      val data = for {
        interpreter <- ExampleApi.api.interpreter
        result      <- interpreter.execute(query)
        _           <- zio.console.putStrLn(s"${result.data}")
      } yield result.data.toString

      // TODO use zio tests - no scalatest
      // https://zio.dev/docs/howto/howto_test_effects
      //data.provideLayer(zio.console.Console.live) shouldBe "FIXME"
    }
  }
}
