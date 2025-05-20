package com.github.niqdev.parser

import cats.parse.Rfc5234.{ digit, wsp }
import cats.parse.{ Parser, Parser0 }
import cats.syntax.apply._
import cats.syntax.either._

// https://typelevel.org/cats-parse
object CatsParseExample {

  private val dash: Parser[Unit]       = Parser.char('-')
  private val colon: Parser[Unit]      = Parser.char(':')
  private val dropSpaces: Parser[Unit] = wsp.rep.void

  private val dateTimeStringParser: Parser[String] = (
    digit.rep(4, 4) *> dash *>
      digit.rep(2, 2) *> dash *>
      digit.rep(2, 2) *> dropSpaces *>
      digit.rep(2, 2) *> colon *>
      digit.rep(2, 2) *> colon *>
      digit.rep(2, 2)
  ).string

  private val dateTimeFormatter: java.time.format.DateTimeFormatter =
    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd\tHH:mm:ss")

  private val dateTimeParser: Parser[java.time.OffsetDateTime] =
    dateTimeStringParser.mapFilter(s =>
      Either
        .catchNonFatal(
          dateTimeFormatter.parse(s, java.time.LocalDateTime.from(_).atOffset(java.time.ZoneOffset.UTC))
        )
        .leftMap(e => println(e))
        .toOption
    )

  val parser: Parser0[LogExample] = (
    dateTimeParser <* dropSpaces,
    Parser.until(dropSpaces).map(Http.Method.apply)
  ).mapN(LogExample.apply)

}
