---
id: scala-todo
title: TODO
---

> TODO add/review courses and books notes

## Data types

### Option

[ [MyOption](https://github.com/niqdev/scala-fp/blob/master/modules/common/src/main/scala/com/github/niqdev/datatype/MyOption.scala) ]

```scala mdoc
sealed trait MyOption[+A]
case class MySome[A](value: A) extends MyOption[A]
case object MyNone extends MyOption[Nothing]
```

### Either

[ [MyEither](https://github.com/niqdev/scala-fp/blob/master/modules/common/src/main/scala/com/github/niqdev/datatype/MyEither.scala) ]

```scala mdoc
sealed trait MyEither[+E, +A]
case class MyRight[+A](value: A) extends MyEither[Nothing, A]
case class MyLeft[+E](error: E) extends MyEither[E, Nothing]
```

## Data structures

### List

[ [MyList](https://github.com/niqdev/scala-fp/blob/master/modules/common/src/main/scala/com/github/niqdev/datastructure/MyList.scala) | [MyListSpec](https://github.com/niqdev/scala-fp/blob/master/modules/common/src/test/scala/com/github/niqdev/datastructure/MyListSpec.scala) ]

```scala mdoc
sealed trait MyList[+A]
case class MyCons[+A](head: A, tail: MyList[A]) extends MyList[A]
case object MyNil extends MyList[Nothing]
```

### Tree

[ [MyTree](https://github.com/niqdev/scala-fp/blob/master/modules/common/src/main/scala/com/github/niqdev/datastructure/MyTree.scala) ]

```scala mdoc
sealed trait MyTree[+A]
case class MyBranch[A](left: MyTree[A], right: MyTree[A]) extends MyTree[A]
case class MyLeaf[A](value: A) extends MyTree[A]
```

## Algorithms

* [Data Structures & Algorithms in Java](https://www.udemy.com/course/from-0-to-1-data-structures) (Course)

TODO

## Akka

* [Akka in Action](https://amzn.to/2vsTesn) (2016) by Raymond Roestenburg, Rob Bakker, and Rob Williams (Book)
* [Documentation](https://akka.io/docs)
* [Alpakka](https://doc.akka.io/docs/alpakka-kafka/current)
* [akka-seed.g8](https://github.com/niqdev/akka-seed.g8) (Repository)
* [scala-akka-boilerplate](https://github.com/brightwindanalysis/scala-akka-boilerplate) (Repository)

TODO

## Cryptography

* [Serious Cryptography](https://nostarch.com/seriouscrypto) (2017) by Jean-Philippe Aumasson (Book)
* [Java Cryptography Architecture](https://www.udemy.com/course/java-cryptography-architecture-hashing-and-secure-password) (Course)

TODO exercises in Scala
