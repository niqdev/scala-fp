package com.github.niqdev

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

final class SemigroupProp extends Properties("Semigroup") {

  property("startsWith") = forAll { (a: String, b: String) =>
    (a + b).startsWith(a)
  }

}
