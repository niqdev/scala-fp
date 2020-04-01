package com.github.niqdev.scalacheck

import com.github.niqdev.model.models._
import io.circe.{ Decoder, Encoder }
import org.scalacheck.{ Prop, Properties }

final class ModelProp extends Properties("Model") {

  import org.scalacheck.Prop.propBoolean

  property("person") = Prop.forAll(Generators.genPerson) { person =>
    val decoded = Decoder[Person].decodeAccumulating(Encoder[Person].apply(person).hcursor)

    decoded.leftMap { errors =>
      println(errors.toList.mkString(","))
      errors
    }

    ("isValid" |: decoded.isValid) &&
    ("equal" |: decoded.toOption.get == person)
  }

}
