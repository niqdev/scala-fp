package com.github.niqdev.scalacheck

import com.github.niqdev.enumeratum.{ Continent, Gender }
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder, KeyDecoder, KeyEncoder }
//import squants.mass.Mass
//import squants.space.Length

object models {

  case class Person(
    name: NonEmptyString,
//    height: Length,
//    weight: Mass,
    gender: Gender,
    continent: Continent,
    motto: String,
    info: Map[NonEmptyString, String],
    active: Boolean
  )
  object Person {
    import io.circe.refined.{ refinedDecoder, refinedEncoder }

//    implicit val lengthEncoder: Encoder[Length] =
//      Encoder.encodeDouble.contramap[Length](_.value)
//    implicit val lengthDecoder: Decoder[Length] =
//      Decoder.decodeDouble.emapTry(Length.apply)
//
//    implicit val massEncoder: Encoder[Mass] =
//      Encoder.encodeDouble.contramap[Mass](_.value)
//    implicit val massDecoder: Decoder[Mass] =
//      Decoder.decodeDouble.emapTry(Mass.apply)

    implicit val keyMapEncoder: KeyEncoder[NonEmptyString] =
      _.value
    implicit lazy val keyMapDecoder: KeyDecoder[NonEmptyString] =
      NonEmptyString.from(_).toOption

    implicit val personEncoder: Encoder[Person] =
      deriveEncoder[Person]
    implicit val personDecoder: Decoder[Person] =
      deriveDecoder[Person]
  }
}
