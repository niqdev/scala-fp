/*
package com.github.niqdev.scalacheck

import java.io.Serializable
import java.util.UUID

import com.github.ghik.silencer.silent
import com.github.niqdev.scalacheck.GenJson._
import io.circe.{Json, JsonObject}
import org.scalacheck.Gen
import org.scalactic.anyvals.NonEmptyString
import shapeless.labelled.FieldType
import shapeless.{HList, HNil, LabelledGeneric, Lazy, Witness}

case class Example(string: String, uuid: UUID)
// ('string -> String, 'uuid: UUID)
// ('string -> Gen[Sting], 'uuid -> Gen[UUID])
// OVERRIDE generators: default provided
// [ SYMBOL, GEN[*] ].foldLeft(Gen.const(JsonObject.empty)) { (result, data) =>
//    val (key, gen) = data
//    result.flatMap { jsonObject => gen.map(value => jsonObject.add(key, value)) }
// }

trait GenJson1[T] {
  def gen: Gen[Json]
}

trait GenJson2[T] {
  def gen(value: T): Gen[Json]
}

// https://github.com/underscoreio/shapeless-guide-code/blob/solutions/json/src/main/scala/json.scala
object GenJson2 {
  def apply[T](implicit ev: GenJson2[T]): GenJson2[T] = ev

  implicit val alphaNumStrGenJson: GenJson2[Gen[String]] =
    _.map(Json.fromString)

  implicit val uuidGenJson: GenJson2[Gen[UUID]] =
    _.map(value => Json.fromString(value.toString))

  import io.circe.syntax.EncoderOps
  implicit val hNilGenJson: GenJson2[HNil] =
    _ => JsonObject.empty.asJson

  import shapeless.::
  import shapeless.ops.hlist.IsHCons
  import shapeless.record._

  val x = LabelledGeneric[Example].

  implicit def hListGenJson[K <: Symbol, H, T <: HList](
     implicit
     witness: Witness.Aux[K],
     hGenJson: Lazy[GenJson2[H]],
     tGenJson: GenJson2[T]
   ): GenJson2[FieldType[K, H] :: T] = {
    val fieldName = witness.value.name

    new GenJson2[HList] {
      override def gen(value: HList): Gen[Json] =
        value match {
          case h :: t =>
            val hField  = witness.value.name -> hGenJson.value.gen(h)
            val tFields = tGenJson.gen(t).fields
            JsonObject(hField :: tFields)
        }
//      {
//        val h: Gen[Json] = hGenJson.value.gen(value.head)
//        val t: Gen[Json] = tGenJson.gen(value.tail)
//        JsonObject((fieldName, h.asJson) :: t.fields)
//        ???
//      }
    }



    /*
    createObjectEncoder { hlist =>
      val head = hEncoder.value.encode(hlist.head)
      val tail = tEncoder.encode(hlist.tail)
      JsonObject((fieldName, head) :: tail.fields)
    }
 */
    ???
  }
}

@silent
object GenJson1 {

  def apply[T](implicit ev: GenJson1[T]): GenJson1[T] = ev

  def fromString(stringGen: Gen[String]): Gen[Json] =
    stringGen.map(Json.fromString)

  implicit val alphaNumStrGenJson: GenJson1[String] =
    new GenJson1[String] {
      override def gen: Gen[Json] =
        fromString(Gen.alphaNumStr)
    }

  implicit val uuidGenJson: GenJson1[UUID] =
    new GenJson1[UUID] {
      override def gen: Gen[Json] =
        Gen.uuid.map(value => Json.fromString(value.toString))
    }

  import io.circe.syntax.EncoderOps

  implicit val exampleGenJson: GenJson1[Example] =
    new GenJson1[Example] {
      override def gen: Gen[Json] =
        for {
          s <- Gen.alphaNumStr
          u <- Gen.uuid
        } yield JsonObject(
          "string" -> Json.fromString(s),
          "uuid" -> Json.fromString(u.toString)
        ).asJson
    }

  implicit val hNilGenJson: GenJson1[HNil] =
    new GenJson1[HNil] {
      override def gen: Gen[Json] =
        JsonObject.empty.asJson
    }

  /*
  def createObjectEncoder[A](fn: A => JsonObject): JsonObjectEncoder[A] =
    new JsonObjectEncoder[A] {
      def encode(value: A): JsonObject =
        fn(value)
    }
 */

  import shapeless.::

  implicit def hListGenJson[K <: Symbol, H, T <: HList](
    implicit
    witness: Witness.Aux[K],
    hGenJson: Lazy[GenJson1[H]],
    tGenJson: GenJson1[T]
  ): GenJson1[FieldType[K, H] :: T] = {
    val fieldName = witness.value.name

    new GenJson1[HList] {
      override def gen: Gen[Json] = ???
    }
    val head = hGenJson.value.gen
    /*
    createObjectEncoder { hlist =>
      val head = hEncoder.value.encode(hlist.head)
      val tail = tEncoder.encode(hlist.tail)
      JsonObject((fieldName, head) :: tail.fields)
    }
 */
    ???
  }

  implicit def genericGenJson[A, H](
    implicit
    generic: LabelledGeneric.Aux[A, H],
    hGenJson: Lazy[GenJson1[H]]
  ): GenJson1[A] = ???

  /*
  createObjectEncoder { value =>
    hEncoder.value.encode(generic.to(value))
  }
 */
}

// shapeless LabelledGeneric get representation without concrete instance
// TODO see random gen
object HelloGenJson1 extends App {

  println(GenJson1.fromString(Gen.numStr).sample)
  println(GenJson1.fromString(Gen.alphaStr).sample)
  println(GenJson1[String].gen.sample)
  println(GenJson1[UUID].gen.sample)
  println(GenJson1[Example].gen.sample)
  println(LabelledGeneric[Example].to(Example("aaa", UUID.randomUUID())))
}

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
 */
