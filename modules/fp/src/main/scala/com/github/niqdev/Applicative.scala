package com.github.niqdev

trait Semigroupal[F[_]] {
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
}

trait Apply[F[_]] extends Semigroupal[F] with Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]

  override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    ap(map(fa)(a => (b: B) => (a, b)))(fb)
}

// TODO laws
// https://en.wikibooks.org/wiki/Haskell/Applicative_functors
// https://github.com/barambani/laws/blob/master/src/main/scala/ApplicativeModule.scala
trait Applicative[F[_]] extends Apply[F] {
  def pure[A](a: A): F[A]

  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    ap(pure(f))(fa)
}

object Applicative {

  object instances extends ApplicativeInstances

  def apply[F[_]](implicit ev: Applicative[F]): Applicative[F] = ev
}

trait ApplicativeInstances {

  implicit val idApplicative: Applicative[Id] =
    new Applicative[Id] {
      override def pure[A](a: A): Id[A] = a

      override def ap[A, B](ff: Id[A => B])(fa: Id[A]): Id[B] = ff(fa)
    }

  implicit val myListApplicative: Applicative[MyList] =
    new Applicative[MyList] {
      override def pure[A](a: A): MyList[A] =
        MyCons(a, MyNil)

      override def ap[A, B](ff: MyList[A => B])(fa: MyList[A]): MyList[B] =
        ff.flatMap(f => fa.map(a => f(a)))
    }

  // TODO
  implicit val myTreeApplicative: Applicative[MyTree] = ???

  implicit val myOptionApplicative: Applicative[MyOption] =
    new Applicative[MyOption] {
      override def pure[A](a: A): MyOption[A] =
        MyOption(a)

      override def ap[A, B](ff: MyOption[A => B])(fa: MyOption[A]): MyOption[B] =
        ff.flatMap(f => fa.map(a => f(a)))
    }

  // * is a kind-projector magic
  implicit def myEitherApplicative[E : Semigroup]: Applicative[MyEither[E, *]] =
    new Applicative[MyEither[E, *]] {
      override def pure[A](a: A): MyEither[E, A] =
        MyRight(a)

      override def ap[A, B](ff: MyEither[E, A => B])(fa: MyEither[E, A]): MyEither[E, B] =
        (ff, fa) match {
          case (MyRight(f), MyRight(a)) =>
            MyRight(f(a))
          case (MyLeft(ef), MyLeft(ea)) =>
            MyLeft(Semigroup[E].combine(ef, ea))
          case (e@MyLeft(_), _) => e
          case (_, e@MyLeft(_)) => e
        }
    }

  implicit def function1Applicative[T]: Applicative[T => *] =
    new Applicative[Function[T, *]] {
      override def pure[A](a: A): Function[T, A] =
        (_: T) => a

      override def ap[A, B](ff: Function[T, A => B])(fa: Function[T, A]): Function[T, B] =
        (t: T) => ff(t)(fa(t))
    }
}
