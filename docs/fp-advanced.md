---
id: fp-advanced
title: Advanced
---

## Resources

* [Functional Programming in Scala](https://amzn.to/2OCFpQG) (2014) by Paul Chiusano and Runar Bjarnason (Book)
* [Functional Programming, Simplified](https://amzn.to/2OCFROS) (2017) by Alvin Alexander (Book)
* **TODO** *"FP explained to ..."* by [Marc-Daniel Ortega](https://github.com/globulon) (Book)(Unpublished)
* [Constraints Liberate, Liberties Constrain](https://www.youtube.com/watch?v=GqmsQeSzMdw) by Runar Bjarnason (Video)
* [Scalaz Presentation](https://vimeo.com/10482466) by Nick Partridge (Video)
* **TODO** [Functional Structures in Scala](https://www.youtube.com/playlist?list=PLFrwDVdSrYE6dy14XCmUtRAJuhCxuzJp0) (Video)
* [Functional Programming with Effects](https://www.youtube.com/watch?v=po3wmq4S15A) by Rob Norris (Video)

## Type Classes

> TODO fix links

* [Functors, Applicatives, And Monads In Pictures](http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html)

### Show

[ [Show](https://niqdev.github.io/scala-fp) | [ShowSpec](https://niqdev.github.io/scala-fp) | [cats.ShowSpec](https://niqdev.github.io/scala-fp) ]

`Show` provides textual representation

```scala mdoc
trait Show[T] {
  def show(value: T): String
}
```

### Eq

[ [cats.EqSpec](https://niqdev.github.io/scala-fp) ]

`Eq` provides **equality** between two instances of the same type

```scala mdoc
trait Eq[A] {
  def eqv(x: A, y: A): Boolean
  def neqv(x: A, y: A): Boolean = !eqv(x, y)
}
```

### Semigroup

[ [Semigroup](https://niqdev.github.io/scala-fp) | [SemigroupSpec](https://niqdev.github.io/scala-fp) | [SemigroupLawsProp](https://niqdev.github.io/scala-fp) ]

`Semigroup` provides **combine** which must be associative

```scala mdoc
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
```

### Monoid

[ [Monoid](https://niqdev.github.io/scala-fp) | [MonoidSpec](https://niqdev.github.io/scala-fp) | [MonoidLawsProp](https://niqdev.github.io/scala-fp) | [cats.MonoidSpec](https://niqdev.github.io/scala-fp) ]

`Monoid` is a `Semigroup` with **empty** which must be an identity element

```scala mdoc
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}
```

* [On Monoids](https://apocalisp.wordpress.com/2010/06/14/on-monoids)
* [fpinscala](https://github.com/fpinscala/fpinscala/wiki/Chapter-10:-Monoids)

### Functor

[ [Functor](https://niqdev.github.io/scala-fp) | [FunctorSpec](https://niqdev.github.io/scala-fp) | [FunctorLawsProp](https://niqdev.github.io/scala-fp) | [cats.FunctorSpec](https://niqdev.github.io/scala-fp) ]

Covariant `Functor` provides **map** which encapsulates sequencing computations

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

### Semigroupal

[ [cats.SemigroupalSpec](https://niqdev.github.io/scala-fp) ]

`Semigroupal` allows to combine contexts (`Semigroup` allows to combine values)

```scala mdoc
trait Semigroupal[F[_]] {
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
}
```

Parameters `fa` and `fb` are independent effectful values: they can be computed in either order before passing them to product, in contrast to `flatMap` which imposes a strict order on its parameters

### Apply

`Apply` allows to apply a parameter to a function within a context

```scala mdoc
trait Apply[F[_]] extends Semigroupal[F] with Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
}
```

### Applicative Functor

> TODO laws

[ [Applicative](https://niqdev.github.io/scala-fp) | [ApplicativeSpec](https://niqdev.github.io/scala-fp) ]

`Applicative` allows to lift a value into a context

```scala mdoc
trait Applicative[F[_]] extends Apply[F] {
  def pure[A](a: A): F[A]
}
```

* [The Essence of the Iterator Pattern](https://www.cs.ox.ac.uk/jeremy.gibbons/publications/iterator.pdf) (Paper)
* [Applicative programming with effects](http://www.staff.city.ac.uk/~ross/papers/Applicative.pdf) (Paper)

### Monad

[ [Monad](https://niqdev.github.io/scala-fp) | [MonadSpec](https://niqdev.github.io/scala-fp) | [MonadLawsProp](https://niqdev.github.io/scala-fp) | [cats.MonadSpec](https://niqdev.github.io/scala-fp) ]

A `Monad` is a mechanism for strictly sequencing effects, also called *monadic behaviour*

```scala mdoc
trait Monad[F[_]] extends Applicative[F] {
  // bind or >>=
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}
```

* [Monads for functional programming](http://homepages.inf.ed.ac.uk/wadler/papers/marktoberdorf/baastad.pdf) (Paper)
* [First steps with monads in Scala](https://darrenjw.wordpress.com/2016/04/15/first-steps-with-monads-in-scala)
* [Demystifying the Monad in Scala](https://medium.com/@sinisalouc/demystifying-the-monad-in-scala-cc716bb6f534)
* [Cooking with Monads](https://www.becompany.ch/en/blog/2016/11/08/cooking-with-monads)
* [Refactoring with Monads](https://typelevel.org/blog/2018/08/07/refactoring-monads.html)
* [Monad laws in Scala](https://devth.com/2015/monad-laws-in-scala)

Everyday monads

* `List`
* `Option`
* `Either`

#### Identity

[ [cats.IdSpec](https://niqdev.github.io/scala-fp) ]

`Id` monad allows to call monadic methods using plain values

```scala mdoc
type Id[A] = A
```

#### MonadError

[ [cats.MonadErrorSpec](https://niqdev.github.io/scala-fp) ]

`MonadError` abstracts over Either-like data types that are used for error handling and provides extra operations for raising and handling errors

```scala mdoc
trait ApplicativeError[F[_], E] extends Applicative[F] {
  def raiseError[A](e: E): F[A]
  def handleError[A](fa: F[A])(f: E => A): F[A]
}

trait MonadError[F[_], E] extends ApplicativeError[F, E] with Monad[F] {
  def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
}
```

* [Rethinking MonadError](https://typelevel.org/blog/2018/04/13/rethinking-monaderror.html)

### Free Monad

* [Stack Safety for Free](https://web.archive.org/web/20190611214133/http://functorial.com/stack-safety-for-free/index.pdf) (Paper)
* [Stackless Scala With Free Monads](http://blog.higher-order.com/assets/trampolines.pdf) (Paper)
* [Move Over Free Monads: Make Way for Free Applicatives!](https://youtu.be/H28QqxO7Ihc) (Video)
* [Stackless Scala](http://www.marcoyuen.com/articles/2016/09/08/stackless-scala-1-the-problem.html)
* [Free monads - what? and why?](https://softwaremill.com/free-monads)
* [Free Monad examples](https://github.com/kenbot/free)
* [Overview of free monad in cats](https://blog.scalac.io/2016/06/02/overview-of-free-monad-in-cats.html)
* Modern Functional Programming [[part-1](http://degoes.net/articles/modern-fp)|[part-2](http://degoes.net/articles/modern-fp-part-2)]

## Data Types

It is impossible to combine two arbitrary monads (write a general definition of `flatMap`) without knowing something about at least one of them. **Monad Transformers** to the rescue! By convention their name ends with a `T` suffix

Common Monad Transformers are

* `OptionT` for `Option`
* `EitherT` for `Either`
* `ReaderT` for `Reader`
* `WriterT` for `Writer`
* `StateT` for `State`

### Eval

[ [cats.EvalSpec](https://niqdev.github.io/scala-fp) ]

`Eval` monad controls eager, lazy, and memoized **evaluation**

```scala mdoc
trait Eval[A] {
  def value: A
  def memoize: Eval[A]
}
```

One useful property of `Eval` is that its `map` and `flatMap` methods are **trampolined**. This means that arbitrarily nested calls don't consume stack frames i.e. it's *stack safety*

`Eval` monad is a useful tool to enforce stack safety when working on very large computations and data structures. Trampolining is not free although, it avoids consuming stack by creating a chain of function objects on the heap. There are still limits on how deeply computations can be nested, but they are bounded by the size of the heap rather than the stack

### State

[ [cats.StateSpec](https://niqdev.github.io/scala-fp) ]

A `State[S, A]` represents a monadic wrapper of a function `S => (S, A)`

```scala mdoc
// IndexedStateT[Eval, S, S, A]
type State[S, A] = StateT[Eval, S, A]

// alias
type StateT[F[_], S, A] = IndexedStateT[F, S, S, A]

class IndexedStateT[F[_], SA, SB, A](val runF: F[SA => F[(SB, A)]])
```

`State` monad allows to model mutable state in a purely functional way, without using mutation

### Writer

[ [cats.WriterSpec](https://niqdev.github.io/scala-fp) ]

A `Writer[L, V]` represents a monadic wrapper of a value `(L, V)`

```scala mdoc
type Writer[L, V] = WriterT[Id, L, V]

class WriterT[F[_], L, V](run: F[(L, V)])
```

`Writer` monad lets carry a log along with a computation. It's useful to record messages, errors, or additional data about a computation, and extract the log alongside the final result

### Kleisli

> TODO

[ [TODO](https://niqdev.github.io/scala-fp) ]

A `Kleisli[F, A, B]` represents a monadic wrapper of a function `A => F[B]`

```scala mdoc
class Kleisli[F[_], A, B](run: A => F[B])
```

### Reader

[ [cats.ReaderSpec](https://niqdev.github.io/scala-fp) ]

A `Reader[A, B]` represents a monadic wrapper of a function `A => B`

```scala mdoc
// Kleisli[Id, E, A]
type Reader[A, B] = ReaderT[Id, A, B]

// alias
type ReaderT[F[_], A, B] = Kleisli[F, A, B]
```

`Reader` monad allows to sequence operations that depend on some input and one common use is dependency injection

### OptionT

[ [cats.OptionTSpec](https://niqdev.github.io/scala-fp) ]

A `OptionT[F[_], A]` represents a monadic wrapper of a value `F[Option[A]]`

```scala mdoc
class OptionT[F[_], A](value: F[Option[A]])
```

`OptionT` is a monad transformer for `Option`

### Validated

[ [cats.ValidatedSpec](https://niqdev.github.io/scala-fp) ]

`Validated` is similar to `Either` but allows to accumulate errors

```scala mdoc
sealed abstract class Validated[+E, +A]
case class Valid[+A](a: A) extends Validated[Nothing, A]
case class Invalid[+E](e: E) extends Validated[E, Nothing]
```

The `Semigroupal` implementation of `product` for a `Monad` is equivalent to `flatMap`, which explains why `Either`, being a `Monad` provides a fail-fast error handling. On the other side, `Validated` has an instance of `Semigroupal` but *no* instance of `Monad`, hence the implementation of `product` is free to accumulate errors

### Arrow

> TODO

* [Functional Composition](https://benmosheron.gitlab.io/blog/2018/12/24/functional-composition.html)

## Effects

Given a monad `M[A]`, if you can *not* extract the `A` out of `M[A]` in a purely-functional way (for `List`you can perform the extraction with `List.head`) then the monad is called **effectful**

* [Cats Effect](https://typelevel.org/cats-effect) (Documentation)
* [FP to the Max](https://youtu.be/sxudIMiOo68) by John De Goes (Video)
* [Intro to Functional Game Programming](https://github.com/jdegoes/lambdaconf-2014-introgame)
* [Incremental Purity](https://git.io/fp017)(https://git.io/fp01d)] (Meetup)
* **TODO** [Shared State in Functional Programming](https://typelevel.org/blog/2018/06/07/shared-state-in-fp.html)

### IO

`IO` is data type for encoding side effects as pure values

* [The Making of an IO](https://www.youtube.com/watch?reload=9&v=g_jP47HFpWA) (Video)
* [An IO monad for cats](https://typelevel.org/blog/2017/05/02/io-monad-for-cats.html)
* [Monadic IO: Laziness Makes You Free](https://underscore.io/blog/posts/2015/04/28/monadic-io-laziness-makes-you-free.html)

> TODO add more examples

```bash
# cats-effect examples
sbt "ecosystem/runMain com.github.niqdev.effect.ExampleIO"
sbt "ecosystem/runMain com.github.niqdev.effect.ExampleIOApp"
sbt "ecosystem/runMain com.github.niqdev.effect.ExampleResource"
sbt "test:testOnly *MyResourceSpec"
```
