package com.github.niqdev.scalacheck

import java.io.Serializable

import com.github.niqdev.scalacheck.GenJson._
import io.circe.Json
import org.scalacheck.Gen
import org.scalactic.anyvals.NonEmptyString

// TODO
trait ScalaCheckJson[T] {
  def gen(item: T): Gen[Json]
}

sealed abstract class GenJson extends Product with Serializable

// TODO KEY = NonEmptyString newtype
object GenJson {
  final case class GenBoolean(key: String)             extends GenJson
  final case class GenNumber(key: String)              extends GenJson // parametrize: Int, Double ...
  final case class GenString(key: String)              extends GenJson
  final case class Gen[T](key: String)                 extends GenJson
  final case class GenArray[T <: GenJson](key: String) extends GenJson
  final case class GenObject(value: GenJson*)          extends GenJson
}

object GenJson0 {
  def apply(gen: Gen[Json]*): Gen[Json] = ???

  def apply[T](key: String): Gen[Json] = ???

  def apply[T](key: String, value: T): Gen[Json] = ???
}

object Hello {

  val expected =
    """
      |{
      |  "myBoolean": true,
      |  "myInt": 123,
      |  "myString": "aaa",
      |  "myArray": [
      |    "a",
      |    "b",
      |    "c"
      |  ],
      |  "myObject": {
      |    "property1": "xxx",
      |    "property2": 456
      |  }
      |}
      |""".stripMargin

  lazy val genJson = GenObject(
    GenBoolean("myBoolean"),
    GenNumber("myInt"),
    GenString("myString"),
    GenArray[GenString]("myArray"),
    GenObject(
      GenString("property1"),
      GenNumber("property2")
    )
  )

  // TODO allow constants in syntax i.e. Gen.const
  implicit class ScalaCheckJsonSyntax(key: String) {
    def gen[T]: Gen[Json]           = ???
    def gen[T](value: T): Gen[Json] = ???
  }

  case class MyProperty(property1: String, property2: Int)

  lazy val genJson0 = GenJson0(
    GenJson0[Boolean]("myBoolean"),
    GenJson0[Int]("myInt"),
    GenJson0[String]("myString"),
    GenJson0[NonEmptyString]("myNonEmptyString"),
    GenJson0[List[String]]("myArray"),
    GenJson0[MyProperty]("myObject"),
    GenJson0[String]("myConstant", "constant")
  )

  lazy val genJson0Syntax = GenJson0(
    "myBoolean".gen[Boolean],
    "myInt".gen[Int],
    "myString".gen[String],
    "myNonEmptyString".gen[NonEmptyString],
    "myArray".gen[List[String]],
    "myObject".gen[MyProperty],
    "myConstant".gen[String]("constant")
  )

}
