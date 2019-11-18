package com.github.niqdev
package laws

import com.github.niqdev.Monoid.instances._
import org.scalacheck.Arbitrary.{ arbBool, arbInt, arbOption, arbString }
import org.scalacheck.Prop.forAll
import org.scalacheck.{ Arbitrary, Properties }

sealed abstract class MonoidLawsProp[A](description: String)(implicit arbitrary: Arbitrary[A], ev: Monoid[A])
    extends Properties(s"MonoidLaws: $description") {

  property("associativity") = forAll { (x: A, y: A, z: A) =>
    MonoidLaws[A].semigroupAssociativity(x, y, z)
  }

  property("leftIdentity") = forAll { a: A =>
    MonoidLaws[A].monoidLeftIdentity(a)
  }

  property("rightIdentity") = forAll { a: A =>
    MonoidLaws[A].monoidRightIdentity(a)
  }
}

object IntAdditionMonoidLawsProp extends MonoidLawsProp[Int]("intAddition") (arbInt, intAdditionMonoid)

object IntMultiplicationMonoidLawsProp
    extends MonoidLawsProp[Int]("intMultiplication") (arbInt, intMultiplicationMonoid)

object StringConcatenationMonoidLawsProp
    extends MonoidLawsProp[String]("stringConcatenation") (arbString, stringConcatenationMonoid)

object BooleanAndMonoidLawsProp extends MonoidLawsProp[Boolean]("booleanAnd") (arbBool, booleanAndMonoid)

object BooleanOrMonoidLawsProp extends MonoidLawsProp[Boolean]("booleanOr") (arbBool, booleanOrMonoid)

object SetUnionMonoidLawsProp
    extends MonoidLawsProp[Set[Int]]("setUnion") (Arbitrary.arbContainer[Set, Int], setUnionMonoid)

object OptionMonoidLawsProp extends MonoidLawsProp[Option[String]]("option") (arbOption, optionMonoid)
