package com.github.niqdev.parser

class ParserTest extends munit.ScalaCheckSuite {

  test("parse example with cats-parse") {
    val result = CatsParseExample.parser.parseAll(Fixtures.example)
    assertEquals(result.toOption.get, Fixtures.exampleExpected)
  }

  test("parse example with parboiled") {
    val result = new ParboiledExample(Fixtures.example).InputLine.run()
    assertEquals(result.get, Fixtures.exampleExpected)
  }

  // TODO fix dateTime generator
//  test("parse generated example with cats-parse") {
//    org.scalacheck.Prop.forAll(Generators.example) { case (in, expected) =>
//      val result = CatsParseExample.parser.parseAll(in).map(out => (in, out))
//      assertEquals(result, Right((in, expected)))
//    }
//  }
}
