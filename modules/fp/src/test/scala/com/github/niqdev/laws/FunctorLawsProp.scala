package com.github.niqdev
package laws

import com.github.niqdev.datastructure.{MyList, MyTree}
import com.github.niqdev.datatype.MyOption
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}

// TODO MyEitherFunctorLawsProp and Function1FunctorLawsProp with kind-projector
// fix A, B, C
sealed abstract class FunctorLawsProp[F[_]](description: String)
                                           (implicit ev: Functor[F],
                                            arbitraryFA: Arbitrary[F[String]])
  extends Properties(s"FunctorLaws: $description") {

  property("identity") = forAll { fa: F[String] =>
    FunctorLaws[F].identityLaw(fa)
  }

  property("composition") = forAll { (fa: F[String], f: String => Int, g: Int => Double) =>
    FunctorLaws[F].compositionLaw(fa, f, g)
  }
}

object MyListFunctorLawsProp
  extends FunctorLawsProp[MyList]("myList")(MyList.instances.myListFunctor, ArbitraryImplicits.arbMyList)

object MyTreeFunctorLawsProp
  extends FunctorLawsProp[MyTree]("myTree")(MyTree.instances.myTreeFunctor, ArbitraryImplicits.arbMyTree)

object MyOptionFunctorLawsProp
  extends FunctorLawsProp[MyOption]("myOption")(MyOption.instances.myOptionFunctor, ArbitraryImplicits.arbMyOption)
