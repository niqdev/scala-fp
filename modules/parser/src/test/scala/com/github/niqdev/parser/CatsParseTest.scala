package com.github.niqdev.parser

class CatsParseTest extends munit.ScalaCheckSuite {

  test("parse example") {
    val result = CatsParse.exampleParser.parseAll(Fixtures.example)
    assertEquals(result.toOption.get, Fixtures.exampleExpected)
  }

  // TODO fix dateTime generator
//  test("parse generated") {
//    org.scalacheck.Prop.forAll(Generators.example) { case (in, expected) =>
//      val result = CatsParse.exampleParser.parseAll(in).map(out => (in, out))
//      assertEquals(result, Right((in, expected)))
//    }
//  }
}
