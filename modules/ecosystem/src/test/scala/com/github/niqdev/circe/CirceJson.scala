package com.github.niqdev.circe

import eu.timepit.refined.string.Uuid
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.{ Json, JsonObject }
import org.scalacheck.Gen

// TODO see ScalaCheckJson
trait CirceJson[T] {
  def gen(value: T): Gen[Json]
}

object CirceJson {

  def apply[T](implicit ev: CirceJson[T]): CirceJson[T] = ev

  implicit val stringCirceJson: CirceJson[String] =
    value => Gen.const(Json.fromString(value))

  implicit val longCirceJson: CirceJson[Long] =
    value => Gen.const(Json.fromLong(value))

  implicit val uuidCirceJson: CirceJson[Uuid] =
    value => Gen.const(Json.fromString(value.toString))

  implicit def genCirceJson[T](
    implicit circeJson: CirceJson[T]
  ): CirceJson[Gen[T]] =
    _.flatMap(circeJson.gen)

  // TODO
  def apply(values: (NonEmptyString, Gen[Json])*): Gen[JsonObject] =
    values.foldLeft(Gen.const(JsonObject.empty)) { (result, data) =>
      val (key, gen) = data
      result.flatMap(jsonObject => gen.map(value => jsonObject.add(key.value, value)))
    }
}

object CirceJsonApp extends App {

  // TODO newtype Key
  implicit class KeyOps(value: String) {
    val key: NonEmptyString =
      NonEmptyString.unsafeFrom(value)
  }

  println(
    CirceJson(
      "aaa".key -> CirceJson[Gen[String]].gen(Gen.alphaNumStr)
    ).sample
  )
}
