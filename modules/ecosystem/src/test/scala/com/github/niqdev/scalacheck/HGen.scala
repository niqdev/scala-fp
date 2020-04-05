package com.github.niqdev.scalacheck

import com.github.ghik.silencer.silent
import io.circe.Json
import org.scalacheck.Gen
import shapeless.labelled.FieldType
import shapeless.{HList, HNil, Lazy, Witness}

object MyGenerators {
  lazy val genBoolean: Gen[Boolean] =
    Gen.oneOf(true, false)
}

// TODO refactor To[circe] To[spray-json] To[avro4s] To[scodec]
trait ToJson[T] {
  def from(tGen: Gen[T]): Gen[Json]
}

// TODO it should relay on existing codec, no manual
object ToJson {
  def apply[T](implicit ev: ToJson[T]): ToJson[T] = ev

  implicit val stringToJson: ToJson[String] =
    _.map(Json.fromString)

  implicit val intToJson: ToJson[Int] =
    _.map(Json.fromInt)

  implicit val booleanToJson: ToJson[Boolean] =
    _.map(Json.fromBoolean)
}

trait HGen[T] {
  def gen: Gen[Json]
}

@silent
object HGen {
  def apply[T](implicit ev: HGen[T]): HGen[T] = ev

  def instance[T](f: =>Gen[Json]): HGen[T] =
    new HGen[T] {
      override def gen: Gen[Json] = f
    }

  val defaultStringHGen: HGen[String] =
    gmap[String](Gen.alphaNumStr)

  val defaultIntHGen: HGen[Int] =
    gmap[Int](Gen.posNum[Int])

  val defaultBooleanHGen: HGen[Boolean] =
    gmap[Boolean](MyGenerators.genBoolean)

  def gmap[T: ToJson](tGen: Gen[T]): HGen[T] =
    instance[T](ToJson[T].from(tGen))

  // TODO
  import shapeless.::
  implicit val hNilHGen: HGen[HNil] =
    instance(Json.Null)

  implicit def hListHGen[K <: Symbol, H, T <: HList](
    implicit
    witness: Witness.Aux[K],
    hHGen: Lazy[HGen[H]],
    tHGen: HGen[T],
    toJson: ToJson[H]
  ): HGen[FieldType[K, H] :: T] = {
    val fieldName = witness.value.name

    // TODO iterate over Repr

    new HGen[FieldType[K, H] :: T] {
      override def gen: Gen[Json] = {
        val head = hHGen.value.gen
        val tail = tHGen.gen

        ???
      }
    }
  }

  implicit def genericHGen[T]: HGen[T] = ???
}

case class Example(myString: String, myInt: Int)

object TestApp extends App {

  HGen.defaultStringHGen.gen
  HGen.gmap[String](Gen.numStr).gen
  HGen[Example].gen
}
