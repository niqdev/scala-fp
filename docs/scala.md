---
id: scala
title: Introduction
---

## Resources

* [Programming in Scala](https://amzn.to/2OAAgc3) (2016)(3rd) by Martin Odersky, Lex Spoon, and Bill Venners (Book)
* [Scala High Performance Programming](https://amzn.to/2Oy7QiS) (2016) by Vincent Theron, Michael Diamant (Book)
* [Scala Puzzlers](http://scalapuzzlers.com)
* [Tour of Scala](https://docs.scala-lang.org/tour/tour-of-scala.html) (Documentation)
* [Scala Improvement Proposals](https://docs.scala-lang.org/sips/all.html)
* [Twitter Scala School](https://twitter.github.io/scala_school)
* [S-99: Ninety-Nine Scala Problems](http://aperiodic.net/phil/scala/s-99)
* [Scala Exercises](https://www.scala-exercises.org)
* [Scala for Project Euler](https://pavelfatin.com/scala-for-project-euler)
* [Scala Collections performance characteristics](https://docs.scala-lang.org/overviews/collections/performance-characteristics.html)
* [The Neophyte's Guide to Scala](https://danielwestheide.com/scala/neophytes.html)
* [Scala Compiler Phases with Pictures](https://www.iteratorshq.com/blog/scala-compiler-phases-with-pictures)

## Related projects

* [sbt](https://www.scala-sbt.org/1.x/docs) - The interactive build tool
* [Dotty](http://dotty.epfl.ch) - A next-generation compiler for Scala
* [Scaladex](https://index.scala-lang.org) - The Scala Library Index
* [ScalaFiddle](https://scalafiddle.io) - An online playground for creating, sharing and embedding Scala fiddles
* [Scastie](https://scastie.scala-lang.org) - Scastie is an interactive playground for Scala with support for sbt configuration
* [Ammonite](http://ammonite.io) - Scala Scripting
* [Scala Native](http://www.scala-native.org/en/latest) - An optimizing ahead-of-time compiler and lightweight managed runtime
* [Scala.js](https://www.scala-js.org) - Optimizes Scala code into highly efficient JavaScript

## FAQ

### *What is the Scala hierarchy?*

![scala-hierarchy](assets/scala-hierarchy.png)

### *What is the difference between by-name and by-value parameters?*

A *by-value* parameter is evaluated before the method is invoked e.g. `(x: Int)` while a *by-name* parameter is not evaluated before the method is invoked, but each time the parameter is referenced inside the method e.g. `(x: => Int)`

### *What are the differences between `def val var lazy`?*

* `def` defines a method
* `val` defines a fixed value, it is immmutable and eagerly initialized
* `var` defines a variable reference, it is mutable and *it should be avoided*
* `lazy` only initialised when required and as late as possible (deferred evaluation), default is strict and it's not recomputed like by-name parameters

### *What are `Nothing Nil None Empty Null null Unit`?*

* `Nothing` is a trait that is the bottom subtype of every subtype of `Any`
* `Nil` is an empty list that is defined as a `List[Nothing]`
* `None` is an empty option that is defined as a `Option[Nothing]`
* `Null` is a trait and is the bottom type similar to `Nothing` but only for `AnyRef` not `AnyVal`
* `null` is an instance of the `Null` trait
* `Unit` is a subtype of `AnyVal`, it's only value is `()` and it is not represented by any object in the underlying runtime system. A method with return type `Unit` is analogous to a Java method which is declared `void`

### *What good are right-associative methods?*

All methods whose names end in `:` are *right-associative*. That is, the expression `x :: xs` is actually the method call `xs.::(x)` , which in turn calls the data constructor `::(x, xs)`

### *What is a companion object?*

* [Singleton Objects](https://docs.scala-lang.org/tour/singleton-objects.html) (Documentation)

An object is a class that has exactly one instance. It is created lazily when it is referenced, like a lazy val. An object with the same name as a class is called a *companion object*. A companion class or object can access the private members of its companion

`static` members in Java are modeled as ordinary members of a companion object in Scala

### *What is type inference?*

* [Type inference](https://docs.scala-lang.org/tour/type-inference.html) (Documentation)

The compiler can often *infer* the type of an expression so you don't have to declare it explicitly

```scala mdoc
val myInt = 8
val myString = "hello"
```

### *What does it mean reify a trait?*

You can't call functions on a trait, so you need to create a concrete instance of that trait before you do anything else. This technique is common with the modular programming approach, and it's known as *reifying* the trait. The word reify is defined as "Taking an abstract concept and making it concrete"

```scala mdoc
trait MyTrait {
  def myMethod = "hello"
}

object MyTrait extends MyTrait

MyTrait.myMethod
```

### *What is the relationship between currying and partially applied function?*

* [Currying](https://docs.scala-lang.org/tour/currying.html)
* [How to use partially applied functions in Scala](https://alvinalexander.com/scala/how-to-use-partially-applied-functions-in-scala-syntax-examples)

Currying is a means of transforming a function that takes more than one argument into a chain of calls to functions, each of which takes a single argument

When you call a function that has parameters, you are said to be applying the function to the parameters. When all the parameters are passed to the function, you have *fully applied the function* to all of the parameters. But when you give only a subset of the parameters to the function, the result of the expression is a *partially applied function*

When a method is called with a fewer number of parameter lists, then this will yield a function taking the missing parameter lists as its arguments
```scala mdoc
val sum = (a: Int, b: Int, c: Int) => a + b + c

// fully applied function
sum(1, 2, 3)

// partially applied function
val f = sum(1, 2, _: Int)

f(3)
```

### *What is a variadic function?*

A *variadic function* accepts zero or more arguments. It provides a little syntactic sugar for creating and passing a Seq of elements explicitly. The special `_*` type annotation allows to pass a Seq to a variadic method

```scala mdoc
sealed trait MyList[+A]
case object MyNil extends MyList[Nothing]
case class MyCons[+A](head: A, tail: MyList[A]) extends MyList[A]

object MyList {
  def apply[A](list: A*): MyList[A] =
    if (list.isEmpty) MyNil
    else MyCons(list.head, apply(list.tail: _*))
}

MyList()
MyList("a", "b")
MyList(1, 2, 3)
```

### *What is covariance and contravariance?*

> TODO

* [Covariance and contravariance in Scala](http://blog.kamkor.me/Covariance-And-Contravariance-In-Scala)
* [The Scala Type System: Parameterized Types and Variances](https://blog.codecentric.de/en/2015/03/scala-type-system-parameterized-types-variances-part-1)
* [Cheat Codes for Contravariance and Covariance](http://blog.originate.com/blog/2016/08/10/cheat-codes-for-contravariance-and-covariance)

### *What is autoboxing?*

The JVM defines primitive types (`boolean`, `byte`, `char`, `float`, `int`, `long`, `short` and `double`) that are *stack-allocated* rather than *heap-allocated*. When a generic type is introduced, for example, `scala.collection.immutable.List`, the JVM references an object equivalent, instead of a primitive type. For example, an instantiated list of integers would be heap-allocated objects rather than integer primitives.

The process of converting a primitive to its object equivalent is called **boxing**, and the reverse process is called **unboxing**. Boxing is a relevant concern for performance-sensitive programming because boxing involves heap allocation. In performance-sensitive code that performs numerical computations, the cost of [boxing and unboxing](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html) can can create significant performance slowdowns

### *What is the `@specialized` annotation?*

* [Avoiding unnecessary object instantiation with specialized generics](https://scalac.io/specialized-generics-object-instantiation)
* [Quirks of Scala Specialization](http://aleksandar-prokopec.com/2013/11/03/specialization-quirks.html)

Specialization with `@specialized` annotation, refers to the compile-time process of generating duplicate versions of a generic trait or class that refer directly to a primitive type instead of the associated object wrapper. At runtime, the compiler-generated version of the generic class (or, as it is commonly referred to, the specialized version of the class) is instantiated.

This process eliminates the runtime cost of boxing primitives, which means that you can define generic abstractions while retaining the performance of a handwritten, specialized implementation

### *What is the `@switch` annotation?*

In scenarios involving simple pattern match statements that directly match a value, using `@switch` annotation provides a warning at compile time if the switch can't be compiled to a tableswitch or lookupswitch which procides better performance, because it results in a branch table rather than a decision tree

### *What is the `@inline` annotation?*

* [The Scala 2.12 and 2.13 Inliner and Optimizer](https://www.lightbend.com/blog/scala-inliner-optimizer)

*An annotation on methods that requests that the compiler should try especially hard to inline the annotated method*

Inlining a function means that instead of having a function call resulting in parameters being placed on the stack and an invoke operation occurring, the definition of the function is copied at compile time to where the invocation was made, saving the invocation overhead at runtime

To enable this feature you need to explicitly set the `-optimize` compiler flag

### *What is a value class?*

* [Value classes](https://docs.scala-lang.org/overviews/core/value-classes.html) (Documentation)

The `AnyVal` class can be used to define a *value class*, which is optimized at compile time to avoid the allocation of an instance

```scala
final case class Price(value: BigDecimal) extends AnyVal {
  def lowerThan(p: Price): Boolean = this.value < p.value
}
```

### *What is a sealed trait?*

> TODO

* [Everything You Ever Wanted to Know About Sealed Traits in Scala](https://underscore.io/blog/posts/2015/06/02/everything-about-sealed.html)
* [More on Sealed Traits in Scala](https://underscore.io/blog/posts/2015/06/04/more-on-sealed.html)
