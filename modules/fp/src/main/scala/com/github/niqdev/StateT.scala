package com.github.niqdev

// https://github.com/jdegoes/lambdaconf-2014-introgame#monad-transformers
// https://github.com/alvinj/FPMonadTransformers/blob/master/src/main/scala/monads/StateT.scala
final case class StateT[F[_], S, A] private (run: S => F[(S, A)]) {

  def get: S => F[(S, A)] = run

  def flatMap[B](f: A => StateT[F, S, B])(implicit F: Monad[F]): StateT[F, S, B] =
    StateT { s0: S =>
      // current state
      val fsa: F[(S, A)] = get(s0)
      F.flatMap(fsa) { case (sa, a) =>
        val sb: StateT[F, S, B] = f(a)
        // new state
        val fsb: F[(S, B)] = sb.get(sa)
        fsb
      }
    }

  def map[B](f: A => B)(implicit F: Monad[F]): StateT[F, S, B] =
    flatMap(a => StateT.pure(f(a)))
}

object StateT {
  def pure[F[_], S, A](a: A)(implicit F: Applicative[F]): StateT[F, S, A] =
    StateT(s => F.pure(s -> a))

  def liftF[F[_], S, A](fa: F[A])(implicit F: Functor[F]): StateT[F, S, A] =
    StateT[F, S, A] { s: S =>
      // F[(S, A)]
      F.map(fa)((a: A) => (s, a))
    }
}
