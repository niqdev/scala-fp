package com.github.niqdev.enumeratum

import enumeratum.values.{ StringCirceEnum, StringDoobieEnum, StringEnum, StringEnumEntry }

sealed abstract class Continent(val value: String) extends StringEnumEntry

case object Continent
    extends StringEnum[Continent]
    with StringCirceEnum[Continent]
    with StringDoobieEnum[Continent] {

  val values = findValues

  case object Africa       extends Continent("Africa")
  case object Antarctica   extends Continent("Antarctica")
  case object Asia         extends Continent("Asia")
  case object Australia    extends Continent("Australia")
  case object Europe       extends Continent("Europe")
  case object NorthAmerica extends Continent("North America")
  case object SouthAmerica extends Continent("South America")
}
