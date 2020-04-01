package com.github.niqdev.scalacheck

import org.scalacheck.Gen

object Generators extends CommonGenerators with EnumeratumGenerators with SquantsGenerators {

  val genPerson: Gen[models.Person] =
    for {
      name <- genNonEmptyString
//      height    <- genCentimeters
//      weight    <- genKilograms
      gender    <- genGender
      continent <- genContinent
      motto     <- Gen.alphaNumStr
      info      <- genMap()
      active    <- genBoolean
    } yield models.Person(
      name,
//      height,
//      weight,
      gender,
      continent,
      motto,
      info,
      active
    )

  def genJson[T: io.circe.Encoder](genT: Gen[T]): Gen[io.circe.Json] =
    genT.map(t => io.circe.Encoder[T].apply(t))
}

trait CommonGenerators {
  import eu.timepit.refined.types.string.NonEmptyString

  lazy val genBoolean: Gen[Boolean] =
    Gen.oneOf(true, false)

  lazy val genNonEmptyString: Gen[NonEmptyString] =
    Gen
      .nonEmptyListOf(Gen.alphaNumChar)
      .map(chars => NonEmptyString.unsafeFrom(chars.mkString))

  lazy val genKeyValue: Gen[(NonEmptyString, String)] =
    for {
      key   <- genNonEmptyString
      value <- Gen.alphaNumStr
    } yield key -> value

  def genMap(size: Int = 10): Gen[Map[NonEmptyString, String]] =
    Gen.mapOfN(size, genKeyValue)
}

trait EnumeratumGenerators {
  import com.github.niqdev.enumeratum.{ Continent, Gender }

  lazy val genContinent: Gen[Continent] =
    Gen.oneOf(Continent.values)

  lazy val genGender: Gen[Gender] =
    Gen.oneOf(Gender.values)
}

trait SquantsGenerators {
  import squants.mass.{ Kilograms, Mass }
  import squants.space.{ Centimeters, Length }

  // https://www.guinnessworldrecords.com/world-records/67521-shortest-man-ever
  // https://www.guinnessworldrecords.com/world-records/tallest-man-living
  lazy val genCentimeters: Gen[Length] =
    Gen
      .choose[Double](0, 300)
      .map(value => Centimeters(value))

  // https://www.guinnessworldrecords.com/world-records/heaviest-man
  lazy val genKilograms: Gen[Mass] =
    Gen
      .choose[Double](0, 700)
      .map(value => Kilograms(value))
}
