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

> TODO

`[name][descrition][implementation][tests][links]`

### Show

```scala mdoc
trait Show[T] {
  def show(value: T): String
}
```

### Semigroup
todo

### Monoid

* [fpinscala](https://github.com/fpinscala/fpinscala/wiki/Chapter-10:-Monoids)
* [On Monoids](https://apocalisp.wordpress.com/2010/06/14/on-monoids)
* [Monoid](http://eed3si9n.com/herding-cats/Monoid.html)

### Functor

a class that encapsulates sequencing computations

the contravariant functor, provides an operation called contramap that represents "prepending" an operation to a chain

Invariant functors implement a method called imap that is informally equivalent to a combination of map and contramap

If F is a covariant functor, wherever we have an F[A] and a conversion A => B we can always convert to an F[B]

If F is a contravariant functor, whenever we have a F[A] and a conversion B => A we can convert to an F[B]

invariant functors capture the case where we can convert from F[A] to F[B] via a func on A => B and vice versa via a func on B => A

```
trait Contravariant[F[_]] {
def contramap[A, B](fa: F[A])(f: B => A): F[B]
}
trait Invariant[F[_]] {
def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
}
```

## Effects

* [FP to the Max](https://youtu.be/sxudIMiOo68) by John De Goes (Video)
* TODO [Functional Programming with Effects](https://www.youtube.com/watch?v=po3wmq4S15A)
