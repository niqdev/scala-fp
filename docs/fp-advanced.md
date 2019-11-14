---
id: fp-advanced
title: Advanced
---

## Resources

* [Functional Programming in Scala](https://amzn.to/2OCFpQG) (2014) by Paul Chiusano and Runar Bjarnason (Book)
* [Functional Programming, Simplified](https://amzn.to/2OCFROS) (2017) by Alvin Alexander (Book)
* [Constraints Liberate, Liberties Constrain](https://www.youtube.com/watch?v=GqmsQeSzMdw) by Runar Bjarnason (Video)
* [Scalaz Presentation](https://vimeo.com/10482466) by Nick Partridge (Video)
* TODO [Functional Structures in Scala](https://www.youtube.com/playlist?list=PLFrwDVdSrYE6dy14XCmUtRAJuhCxuzJp0)
* [Scala's Types of Types](https://ktoso.github.io/scala-types-of-types)

## Type Classes

> TODO fix links

### Show

[[Show](https://niqdev.github.io/scala-fp) | [ShowSpec](https://niqdev.github.io/scala-fp) | [cats.ShowSpec](https://niqdev.github.io/scala-fp)]

`Show` provides textual representation

```scala mdoc
trait Show[T] {
  def show(value: T): String
}
```

### Eq

[[cats.EqSpec](https://niqdev.github.io/scala-fp)]

`Eq` provides **equality** between 2 instances of the same type

```scala mdoc
trait Eq[A] {
  def eqv(x: A, y: A): Boolean
  def neqv(x: A, y: A): Boolean = !eqv(x, y)
}
```

### Semigroup

[[Semigroup](https://niqdev.github.io/scala-fp) | [SemigroupSpec](https://niqdev.github.io/scala-fp) | [SemigroupLawsProp](https://niqdev.github.io/scala-fp)]

`Semigroup` provides **combine** which must be associative

```scala mdoc
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
```

### Monoid

[[Monoid](https://niqdev.github.io/scala-fp) | [MonoidSpec](https://niqdev.github.io/scala-fp) | [MonoidLawsProp](https://niqdev.github.io/scala-fp) | [cats.MonoidSpec](https://niqdev.github.io/scala-fp)]

`Monoid` is a `Semigroup` with **empty** which must be an identity element

```scala mdoc
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}
```

> TODO resources

* [On Monoids](https://apocalisp.wordpress.com/2010/06/14/on-monoids)
* TODO [fpinscala](https://github.com/fpinscala/fpinscala/wiki/Chapter-10:-Monoids)
* TODO [Monoid](http://eed3si9n.com/herding-cats/Monoid.html)

### Functor

[[Functor](https://niqdev.github.io/scala-fp) | [FunctorSpec](https://niqdev.github.io/scala-fp) | [FunctorLawsProp](https://niqdev.github.io/scala-fp) | [cats.FunctorSpec](https://niqdev.github.io/scala-fp)]

`Functor` provides **map** which encapsulates sequencing computations

```scala mdoc
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
```

The `Contravariant` functor provides an operation called **contramap** that represents prepending an operation to a chain

```scala mdoc
trait Contravariant[F[_]] {
  def contramap[A, B](fa: F[A])(f: B => A): F[B]
}
```

The `Invariant` functors provides an operation called **imap** that is informally equivalent to a combination of `map` and `contramap`

```scala mdoc
trait Invariant[F[_]] {
  def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
}
```

### Apply

> TODO

### Applicative

> TODO

### Monad

> TL;DR

[[Monad](https://niqdev.github.io/scala-fp) | [MonadSpec](https://niqdev.github.io/scala-fp) | [MonadLawsProp](https://niqdev.github.io/scala-fp) | [cats.MonadSpec](https://niqdev.github.io/scala-fp)]

* A `Monad` is a mechanism for sequencing effects, also called *monadic behaviour*
* a monad is wrapper
* TODO `def flatten[A](v: F[F[A]]): F[A]`

```scala mdoc
trait Monad[F[_]] {
  // point
  def pure[A](value: A): F[A]
  // bind or >>=
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}
```

Everyday monads

* `List`
* `Option`
* `Either`

#### Identity

[[TODO](https://niqdev.github.io/scala-fp)]

`Id` monad allows to call monadic methods using plain values

```scala mdoc
type Id[A] = A
```

#### MonadError

> TODO ApplicativeError

[[TODO](https://niqdev.github.io/scala-fp)]

```scala mdoc
trait MonadError[F[_], E] extends Monad[F] {
  def raiseError[A](e: E): F[A]
  def handleError[A](fa: F[A])(f: E => A): F[A]
  def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
}
```

#### Eval

[[TODO](https://niqdev.github.io/scala-fp)]

`Eval` monad controls eager, lazy, and memoized **evaluation**

```scala mdoc
trait Eval[+A] {
  def value: A
  def memoize: Eval[A]
}
```

One useful property of `Eval` is that its `map` and `flatMap` methods are **trampolined**. This means that arbitrarily nested calls don't consume stack frames i.e. it's *stack safety*

`Eval` monad is a useful tool to enforce stack safety when working on very large computations and data structures. Trampolining is not free although, it avoids consuming stack by creating a chain of function objects on the heap. There are still limits on how deeply computations can be nested, but they are bounded by the size of the heap rather than the stack

#### State

> TODO transformer

[[TODO](https://niqdev.github.io/scala-fp)]

`State` monad allows to model mutable state in a purely functional way, without using mutation

```scala mdoc
// represents functions of type `S => (S, A)`
type State[S, A] = StateT[Eval, S, A]

/**
 * `StateT[F, S, A]` is similar to `Kleisli[F, S, A]` in that it takes an `S`
 * argument and produces an `A` value wrapped in `F`. However, it also produces
 * an `S` value representing the updated state (which is wrapped in the `F`
 * context along with the `A` value.
 */
type StateT[F[_], S, A] = IndexedStateT[F, S, S, A]

/**
 * `IndexedStateT[F, SA, SB, A]` is a stateful computation in a context `F` yielding
 * a value of type `A`. The state transitions from a value of type `SA` to a value
 * of type `SB`.
 */
class IndexedStateT[F[_], SA, SB, A](val runF: F[SA => F[(SB, A)]])
```

#### Writer

> TODO transformer

[[TODO](https://niqdev.github.io/scala-fp)]

```scala mdoc
case class WriterT[F[_], L, V](run: F[(L, V)]) {}

type Writer[L, V] = WriterT[Id, L, V]
```

`Writer` monad (data type) lets carry a log along with a computation. It's useful to record messages, errors, or additional data about a computation, and extract the log alongside the final result

### Kleisli

#### Reader

> TODO

[[TODO](https://niqdev.github.io/scala-fp)]

```scala
type ReaderT[F[_], -A, B] = Kleisli[F, A, B]

type Reader[-A, B] = ReaderT[Id, A, B]
```

`Reader` monad (data type) allows to sequence operations that depend on some input. One common use for Readers is dependency injection

`A Reader[A, B] is just a monadic wrapper over A => B`

Reader is limited over pure functions

`Kleisli[F, A, B] is just a monadic wrapper over A => F[B]`, a monad transformer for Reader, Kleisli is a ReaderT

The goal - to give monadic interface to functions

### TODO

#### OptionT, EitherT, IorT

### IO

* [FP to the Max](https://youtu.be/sxudIMiOo68) by John De Goes (Video)
* TODO [Functional Programming with Effects](https://www.youtube.com/watch?v=po3wmq4S15A)

TODO
* IO.apply
* IO.pure & unit
* IO.map & flatMap
* IO.raiseError
* IO.attempt
* Synx, Async, Effect
