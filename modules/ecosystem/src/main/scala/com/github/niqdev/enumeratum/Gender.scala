package com.github.niqdev.enumeratum

import enumeratum.values.{ StringCirceEnum, StringDoobieEnum, StringEnum, StringEnumEntry }

sealed abstract class Gender(val value: String) extends StringEnumEntry

case object Gender extends StringEnum[Gender] with StringCirceEnum[Gender] with StringDoobieEnum[Gender] {

  val values = findValues

  case object Male    extends Gender("M")
  case object Female  extends Gender("F")
  case object Unknown extends Gender("U")
}
