package com.github.niqdev

// https://github.com/jdegoes/lambdaconf-2014-introgame#the-state-monad
// https://github.com/alvinj/StateMonadExample/blob/master/src/main/scala/state_monad/State.scala
// https://earldouglas.com/talks/state-monad/slides.html
final case class State[S, A] private(run: S => (S, A)) {

  def get: S => (S, A) = run

  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State { s0: S =>
      // current state
      val (sa, a): (S, A) = get(s0)
      val sb: State[S, B] = f(a)
      // new state
      val s: (S, B) = sb.get(sa)
      s
    }

  def map[B](f: A => B): State[S, B] =
    flatMap(a => State.pure(f(a)))
}

object State {
  def pure[S, A](value: A): State[S, A] =
    State(run = s => (s, value))
}
