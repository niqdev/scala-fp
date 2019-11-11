package com.github.niqdev

import com.github.niqdev.Semigroup.instances.{intAdditionSemigroup, stringConcatenationSemigroup}
import org.scalacheck.Arbitrary.{arbInt, arbString}
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}

sealed abstract class SemigroupLawsProp[A](description: String)
                                          (implicit arbitrary: Arbitrary[A], ev: Semigroup[A])
  extends Properties(s"SemigroupLaws: $description") {

  property("associativity") = forAll { (x: A, y: A, z: A) =>
    SemigroupLaws[A].semigroupAssociativity(x, y, z)
  }

}

object IntAdditionSemigroupLawsProp
  extends SemigroupLawsProp[Int]("intAddition")(arbInt, intAdditionSemigroup)

object StringConcatenationSemigroupLawsProp
  extends SemigroupLawsProp[String]("stringConcatenation")(arbString, stringConcatenationSemigroup)
