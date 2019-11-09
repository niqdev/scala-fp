---
id: scala
title: Scala
---

## Resources

> TODO

## Basic concepts

### *What is the Scala hierarchy?*

![scala-hierarchy](assets/scala-hierarchy.png)

### *What is the difference between by-name and by-value parameters?*

A **by-value** parameter is evaluated before the method is invoked e.g. `(x: Int)` while a **by-name** parameter is not evaluated before the method is invoked, but each time the parameter is referenced inside the method e.g. `(x: => Int)`

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

### *What is a variadic function?*

A **variadic function** accepts zero or more arguments. It provides a little syntactic sugar for creating and passing a Seq of elements explicitly. The special `_*` type annotation allows to pass a Seq to a variadic method

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

### *What is autoboxing?*

The JVM defines primitive types (`boolean`, `byte`, `char`, `float`, `int`, `long`, `short` and `double`) that are *stack-allocated* rather than *heap-allocated*. When a generic type is introduced, for example, `scala.collection.immutable.List`, the JVM references an object equivalent, instead of a primitive type. For example, an instantiated list of integers would be heap-allocated objects rather than integer primitives.

The process of converting a primitive to its object equivalent is called *boxing*, and the reverse process is called *unboxing*. Boxing is a relevant concern for performance-sensitive programming because boxing involves heap allocation. In performance-sensitive code that performs numerical computations, the cost of [boxing and unboxing](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html) can can create significant performance slowdowns

### *What is the `@specialized` annotation?*

**Specialization** with `@specialized` annotation, refers to the compile-time process of generating duplicate versions of a generic trait or class that refer directly to a primitive type instead of the associated object wrapper. At runtime, the compiler-generated version of the generic class (or, as it is commonly referred to, the specialized version of the class) is instantiated.

This process eliminates the runtime cost of boxing primitives, which means that you can define generic abstractions while retaining the performance of a handwritten, specialized implementation

* [Avoiding unnecessary object instantiation with specialized generics](https://scalac.io/specialized-generics-object-instantiation)
* [Quirks of Scala Specialization](http://aleksandar-prokopec.com/2013/11/03/specialization-quirks.html)

### *What is the `@switch` annotation?*

In scenarios involving simple pattern match statements that directly match a value, using `@switch` annotation provides a warning at compile time if the switch can't be compiled to a tableswitch or lookupswitch which procides better performance, because it results in a branch table rather than a decision tree

### *What is the `@inline` annotation?*

*An annotation on methods that requests that the compiler should try especially hard to inline the annotated method*

Inlining a function means that instead of having a function call resulting in parameters being placed on the stack and an invoke operation occurring, the definition of the function is copied at compile time to where the invocation was made, saving the invocation overhead at runtime

To enable this feature you need to explicitly set the `-optimize` compiler flag

* [The Scala 2.12 and 2.13 Inliner and Optimizer](https://www.lightbend.com/blog/scala-inliner-optimizer)

### *What is a value class?*

The `AnyVal` class can be used to define a **value class**, which is optimized at compile time to avoid the allocation of an instance

```scala
final case class Price(value: BigDecimal) extends AnyVal {
  def lowerThan(p: Price): Boolean = this.value < p.value
}
```

* [Value classes](https://docs.scala-lang.org/overviews/core/value-classes.html) (Documentation)
