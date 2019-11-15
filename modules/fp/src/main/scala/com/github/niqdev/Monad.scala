package com.github.niqdev

/**
 * Monad Type Class
 */
trait Monad[F[_]] extends Applicative[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => pure(f(a)))

  /* FlatMap */

  override def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] =
    flatMap(ff)(f => map(fa)(f))

  override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    flatMap(fa)(a => map(fb)(b => a -> b))

  override def map2[A, B, T](fa: F[A], fb: F[B])(f: (A, B) => T): F[T] =
    flatMap(fa)(a => map(fb)(b => f(a, b)))

  def flatten[A](ffa: F[F[A]]): F[A] =
    flatMap(ffa)(fa => fa)
}

object Monad {

  object instances extends MonadInstances

  def apply[F[_]](implicit ev: Monad[F]): Monad[F] = ev
}

trait MonadInstances {

  implicit val idMonad: Monad[Id] =
    new Monad[Id] {
      override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)

      override def pure[A](a: A): Id[A] = a
    }

  implicit val myListMonad: Monad[MyList] =
    new Monad[MyList] {
      override def flatMap[A, B](fa: MyList[A])(f: A => MyList[B]): MyList[B] =
        fa.flatMap(f)

      override def pure[A](a: A): MyList[A] =
        MyList.cons(a)
    }

  // TODO
  implicit lazy val myTreeMonad: Monad[MyTree] = ???

  implicit val myOptionMonad: Monad[MyOption] =
    new Monad[MyOption] {
      override def flatMap[A, B](fa: MyOption[A])(f: A => MyOption[B]): MyOption[B] =
        fa.flatMap(f)

      override def pure[A](a: A): MyOption[A] =
        MyOption(a)
    }

  implicit def myEitherMonad[E]: Monad[MyEither[E, *]] =
    new Monad[MyEither[E, *]] {
      override def flatMap[A, B](fa: MyEither[E, A])(f: A => MyEither[E, B]): MyEither[E, B] =
        fa.flatMap(f)

      override def pure[A](a: A): MyEither[E, A] =
        MyRight(a)
    }

  implicit def function1Monad[T]: Monad[T => *] =
    new Monad[Function[T, *]] {
      override def flatMap[A, B](fa: Function[T, A])(f: A => Function[T, B]): Function[T, B] =
        (t: T) => (fa andThen f)(t)(t)

      override def pure[A](a: A): Function[T, A] =
        (_: T) => a
    }
}
