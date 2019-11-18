package com.github.niqdev

// F stands for effect
// ev stands for evidence
// F[_]: type constructors and higher-kinded types
// import scala.language.higherKinds

/**
  * Functor Type Class
  */
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object Functor {

  object instances extends FunctorInstances
  object syntax    extends FunctorSyntax

  def apply[F[_]](implicit ev: Functor[F]): Functor[F] = ev
}

trait FunctorInstances {

  implicit val myListFunctor: Functor[MyList] =
    new Functor[MyList] {
      override def map[A, B](fa: MyList[A])(f: A => B): MyList[B] =
        fa.map(f)
    }

  implicit val myTreeFunctor: Functor[MyTree] =
    new Functor[MyTree] {
      override def map[A, B](fa: MyTree[A])(f: A => B): MyTree[B] =
        fa match {
          case MyLeaf(a) =>
            MyLeaf(f(a))
          case MyBranch(left, right) =>
            MyBranch(myTreeFunctor.map(left)(f), myTreeFunctor.map(right)(f))
        }
    }

  implicit val myOptionFunctor: Functor[MyOption] =
    new Functor[MyOption] {
      override def map[A, B](fa: MyOption[A])(f: A => B): MyOption[B] =
        fa.map(f)
    }

  // TODO review test MyEither
  // * is a kind-projector magic
  implicit def myEitherFunctor[E]: Functor[MyEither[E, *]] =
    new Functor[MyEither[E, *]] {
      override def map[A, B](fa: MyEither[E, A])(f: A => B): MyEither[E, B] =
        fa.map(f)
    }

  // see catsStdMonadForFunction1
  implicit def function1Functor[T]: Functor[T => *] =
    new Functor[Function[T, *]] {
      override def map[A, B](fa: Function[T, A])(f: A => B): Function[T, B] =
        fa andThen f
    }
}

trait FunctorSyntax {

  implicit final class FunctorOps[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit ev: Functor[F]): F[B] =
      ev.map(fa)(f)
  }
}
