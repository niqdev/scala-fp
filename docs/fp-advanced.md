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

[[Show](https://niqdev.github.io/scala-fp) | [ShowSpec](https://niqdev.github.io/scala-fp)]

```scala mdoc
trait Show[T] {
  def show(value: T): String
}
```

`Show` provides textual representation

### Semigroup

[[Semigroup](https://niqdev.github.io/scala-fp) | [SemigroupSpec](https://niqdev.github.io/scala-fp) | [SemigroupLawsProp](https://niqdev.github.io/scala-fp)]

```scala mdoc
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
```

`Semigroup` provides `combine` which must be associative

### Monoid

[[Monoid](https://niqdev.github.io/scala-fp) | [MonoidSpec](https://niqdev.github.io/scala-fp) | [MonoidLawsProp](https://niqdev.github.io/scala-fp)]

```scala mdoc
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}
```

`Monoid` is a `Semigroup` with `empty` which must be an identity element

* [On Monoids](https://apocalisp.wordpress.com/2010/06/14/on-monoids)
* TODO [fpinscala](https://github.com/fpinscala/fpinscala/wiki/Chapter-10:-Monoids)
* TODO [Monoid](http://eed3si9n.com/herding-cats/Monoid.html)

### Functor

[[Functor](https://niqdev.github.io/scala-fp) | [FunctorSpec](https://niqdev.github.io/scala-fp) | [FunctorLawsProp](https://niqdev.github.io/scala-fp)]

```scala mdoc
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

// not implemented
trait Contravariant[F[_]] {
  def contramap[A, B](fa: F[A])(f: B => A): F[B]
}

// not implemented
trait Invariant[F[_]] {
  def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
}
```

`Functor` provides `map` which encapsulates sequencing computations

The `Contravariant` functor provides an operation called `contramap` that represents prepending an operation to a chain

The `Invariant` functors provides an operation called `imap` that is informally equivalent to a combination of `map` and `contramap`

## Effects

* [FP to the Max](https://youtu.be/sxudIMiOo68) by John De Goes (Video)
* TODO [Functional Programming with Effects](https://www.youtube.com/watch?v=po3wmq4S15A)