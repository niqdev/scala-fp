package com.github.niqdev.scalacheck

import io.circe.Json
import org.scalacheck.Gen

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

object HGen {
  def apply[T](implicit ev: HGen[T]): HGen[T] = ev

  def instance[T](f: =>Gen[Json]): HGen[T] =
    new HGen[T] {
      override def gen: Gen[Json] = f
    }

  val defaultStringHGen: HGen[String] =
    gmap[String](Gen.alphaNumStr)

  val defaultIntHGen: HGen[Int] =
    gmap[Int](Gen.posNum)

  val defaultBooleanHGen: HGen[Boolean] =
    gmap[Boolean](MyGenerators.genBoolean)

  def gmap[T: ToJson](tGen: Gen[T]): HGen[T] =
    instance[T](ToJson[T].from(tGen))

  // TODO
  implicit def genericHGen[T]: HGen[T] = ???
}

case class Example(myString: String, myInt: Int)

object TestApp extends App {

  HGen.defaultStringHGen.gen
  HGen.gmap[String](Gen.numStr).gen
  HGen[Example].gen
}
