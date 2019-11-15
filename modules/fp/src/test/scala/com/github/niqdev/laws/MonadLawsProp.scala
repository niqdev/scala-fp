package com.github.niqdev
package laws

import com.github.niqdev.Monad.instances._
import com.github.niqdev.laws.ArbitraryImplicits._
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}

sealed abstract class MonadLawsProp[F[_], A, B, C](description: String)
                                                  (implicit ev: Monad[F],
                                                   arbitraryA: Arbitrary[A],
                                                   arbitraryFA: Arbitrary[F[A]],
                                                   arbitraryF: Arbitrary[A => F[B]],
                                                   arbitraryG: Arbitrary[B => F[C]])
  extends Properties(s"MonadLaws: $description") {

  property("leftIdentity") = forAll { (a: A, f: A => F[B]) =>
    MonadLaws[F].leftIdentityLaw(a, f)
  }

  property("rightIdentity") = forAll { fa: F[A] =>
    MonadLaws[F].rightIdentityLaw(fa)
  }

  property("associativity") = forAll { (fa: F[A], f: A => F[B], g: B => F[C]) =>
    MonadLaws[F].associativityLaw(fa, f, g)
  }
}

object IdMonadLawsProp extends MonadLawsProp[Id, String, Int, Double]("Id")

// FIXME java.lang.StackOverflowError
//object MyListMonadLawsProp extends MonadLawsProp[MyList, String, Int, Double]("MyList")

object MyOptionMonadLawsProp extends MonadLawsProp[MyOption, Wrapper[String], Int, Double]("MyOption")

// FIXME could not find implicit value for parameter arbitraryFA: org.scalacheck.Arbitrary[com.github.niqdev.datatype.MyEither[String,String]]
//object MyEitherMonadLawsProp extends MonadLawsProp[MyEither[String, *], String, Int, Double]("MyEither")

// FIXME MonadLaws: Falsified
//object Function1MonadLawsProp extends MonadLawsProp[String => *, String, Int, Double]("Function1")
