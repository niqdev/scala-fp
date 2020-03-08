package com.github.niqdev
package laws

import com.github.niqdev.Functor.instances.{ myEitherFunctor, myListFunctor, myOptionFunctor, myTreeFunctor }
import com.github.niqdev.datastructure.{ MyList, MyTree }
import com.github.niqdev.datatype.MyOption
import org.scalacheck.Prop.forAll
import org.scalacheck.{ Arbitrary, Properties }

sealed abstract class FunctorLawsProp[F[_]](description: String)(
  implicit ev: Functor[F],
  arbitraryFA: Arbitrary[F[String]]
) extends Properties(s"FunctorLaws: $description") {

  // A fixed
  property("identity") = forAll { fa: F[String] =>
    FunctorLaws[F].identityLaw(fa)
  }

  // A, B, C fixed
  property("composition") = forAll { (fa: F[String], f: String => Int, g: Int => Double) =>
    FunctorLaws[F].compositionLaw(fa, f, g)
  }
}

object MyListFunctorLawsProp
    extends FunctorLawsProp[MyList]("myList")(myListFunctor, ArbitraryImplicits.arbMyList)

object MyTreeFunctorLawsProp
    extends FunctorLawsProp[MyTree]("myTree")(myTreeFunctor, ArbitraryImplicits.arbMyTree)

object MyOptionFunctorLawsProp
    extends FunctorLawsProp[MyOption]("myOption")(myOptionFunctor, ArbitraryImplicits.arbMyOption)

object MyEitherFunctorLawsProp
    extends FunctorLawsProp[MyEither[String, *]]("myEither")(myEitherFunctor, ArbitraryImplicits.arbMyEither)

// FIXME FunctorLaws: Falsified
//object Function1FunctorLawsProp
//  extends FunctorLawsProp[String => *]("function1")(function1Functor, Arbitrary.arbFunction1)
