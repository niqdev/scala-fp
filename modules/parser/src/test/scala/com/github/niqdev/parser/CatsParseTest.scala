package com.github.niqdev.parser

class CatsParseTest extends munit.ScalaCheckSuite {

  test("parse example") {
    val result = CatsParse.exampleParser.parseAll(Fixtures.example)
    assertEquals(result.toOption.get, Fixtures.exampleExpected)
  }
}
