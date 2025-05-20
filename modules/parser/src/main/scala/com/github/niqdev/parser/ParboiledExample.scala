package com.github.niqdev.parser

import org.parboiled2._
import org.parboiled2.support.hlist.{ ::, HNil }

import java.time.{ OffsetDateTime, ZoneOffset }
import scala.util.{ Failure, Success, Try }

// https://github.com/sirthias/parboiled2
class ParboiledExample(val input: ParserInput) extends Parser {

  private def WS1: Rule0    = rule(oneOrMore(CharPredicate(" \t")))
  private def alphas: Rule0 = rule(oneOrMore(CharPredicate.Alpha))

  private def intConversion: Rule[String :: HNil, Int :: HNil] = rule {
    MATCH ~> { (raw: String) =>
      raw.toIntOption match {
        case None        => failX[HNil, Int :: HNil](s"Invalid int value '$raw'")
        case Some(value) => push(value)
      }
    }
  }

  private def digitsAsInt(n: Int): Rule1[Int] =
    rule(capture(n.times(predicate(CharPredicate.Digit))) ~!~ intConversion)

  private def `yyyy-MM-dd`: Rule[HNil, Int :: Int :: Int :: HNil] =
    rule(digitsAsInt(4) ~!~ '-' ~!~ digitsAsInt(2) ~!~ '-' ~!~ digitsAsInt(2))

  private def `hh:mm:ss`: Rule[HNil, Int :: Int :: Int :: HNil] =
    rule(digitsAsInt(2) ~!~ ':' ~!~ digitsAsInt(2) ~!~ ':' ~!~ digitsAsInt(2))

  private def `yyyy-MM-dd hh:mm:ss`: Rule1[OffsetDateTime] = rule {
    (`yyyy-MM-dd` ~!~ WS1 ~!~ `hh:mm:ss`) ~> {
      (year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) =>
        Try(OffsetDateTime.of(year, month, day, hour, minute, second, 0, ZoneOffset.UTC)) match {
          case Failure(exception) =>
            failX[HNil, OffsetDateTime :: HNil](
              s"Invalid value for Offset Date Time, error ${exception.getMessage}"
            )
          case Success(value) =>
            push(value)
        }
    }
  }

  private def httpMethod: Rule1[Http.Method] =
    rule(capture(alphas) ~> (Http.Method(_)))

  def InputLine: Rule1[LogExample] =
    rule {
      `yyyy-MM-dd hh:mm:ss` ~!~ WS1 ~!~ httpMethod ~ EOI ~> LogExample.apply _
    }
}
