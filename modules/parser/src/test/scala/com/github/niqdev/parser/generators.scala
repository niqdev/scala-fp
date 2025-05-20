package com.github.niqdev.parser

import org.scalacheck.{ Arbitrary, Gen }

import java.time.{ Instant, LocalDateTime, OffsetDateTime, ZoneId, ZoneOffset }

object CommonGenerators {

  // import eu.timepit.refined.scalacheck.string.nonEmptyStringArbitrary

  private val MaxInstant = Instant.parse("9999-12-31T23:59:59.999999999Z")

  def chooseInstant(min: Instant, max: Instant): Gen[Instant] =
    for {
      millis <- Gen.choose(min.getEpochSecond, max.getEpochSecond)
      nanos  <- Gen.choose(min.getNano, max.getNano)
    } yield Instant.ofEpochMilli(millis).plusNanos(nanos.toLong)

  val genInstant: Gen[Instant] =
    chooseInstant(Instant.EPOCH, MaxInstant)

  implicit val arbInstant: Arbitrary[Instant] = Arbitrary(genInstant)

  private val MaxLocalDateTime = LocalDateTime.ofInstant(MaxInstant, ZoneId.of("UTC"))
  private val MinLocalDateTime = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"))

  def chooseLocalDateTime(min: LocalDateTime, max: LocalDateTime): Gen[LocalDateTime] =
    for {
      seconds <- Gen.choose(
        min.toEpochSecond(ZoneOffset.UTC),
        max.toEpochSecond(ZoneOffset.UTC)
      )
      nanos <- Gen.choose(min.getNano, max.getNano)
    } yield LocalDateTime.ofEpochSecond(seconds, nanos, ZoneOffset.UTC)

  val genLocalDateTime: Gen[LocalDateTime] = chooseLocalDateTime(MinLocalDateTime, MaxLocalDateTime)

  implicit lazy val arbLocalDateTime: Arbitrary[LocalDateTime] = Arbitrary(genLocalDateTime)

  import scala.jdk.CollectionConverters._

  val genZoneId: Gen[ZoneId] =
    Gen.oneOf(ZoneId.getAvailableZoneIds.asScala.toSeq.map(ZoneId.of))

  implicit val arbZoneId: Arbitrary[ZoneId] = Arbitrary(genZoneId)

  def chooseOffsetDateTime(min: Instant, max: Instant): Gen[OffsetDateTime] =
    for {
      zoneId  <- genZoneId
      instant <- chooseInstant(min, max)
    } yield OffsetDateTime.ofInstant(instant, zoneId)

  val genOffsetDateTime: Gen[OffsetDateTime] = chooseOffsetDateTime(Instant.EPOCH, MaxInstant)

  implicit lazy val arbOffsetDateTime: Arbitrary[OffsetDateTime] = Arbitrary(genOffsetDateTime)

}

object Generators {

  val genHttpMethod: Gen[Http.Method] = Gen
    .oneOf(
      "POST",
      "GET",
      "DELETE",
      "PUT",
      "OPTIONS",
      "HEAD",
      "TRACE"
    )
    .map(Http.Method.apply)

  val example: Gen[(String, LogExample)] =
    for {
      dateTime <- CommonGenerators.genOffsetDateTime
      method   <- genHttpMethod
    } yield (
      s"$dateTime\t$method",
      LogExample(dateTime, method)
    )
}
