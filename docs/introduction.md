---
id: introduction
title: Introduction
---

## Basic Scala concepts

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

---

## Basic FP concepts

### *What is the uniform access principal?*

The uniform access principle states that variables, precomputed properties and parameterless functions should be accessed using the same syntax. Therefore not betraying whether they are implemented through storage or through computation. Scala supports this principle by not allowing parentheses to be placed at call sites of parameterless functions. A parameterless function definition `def` can be changed to a `val` or vice versa, without affecting client code

### *What referentially transparent means?*

An expression `e` is **referentially transparent** if, for all programs `p`, all occurrences of `e` in `p` can be replaced by the result of evaluating `e` without affecting the meaning of `p`

* [Referential Transparency](https://pierangeloc.github.io/blog/2018/06/01/on-RT-and-FP) (Blog)

### *What is a pure function?*

A function `f` is **pure** if the expression `f(x)` is referentially transparent for all referentially transparent `x`. Hence a pure function is **modular** and **composable**

### *What is a higher-order function?*

A **higher-order function** is a function that takes other functions as arguments or returns a function as result

### *What is recursive function?*

A **recursive function** is a function which calls itself. With **head recursion**, the recursive call is not the last instruction in the function

A **tail recursive function** is a special case of recursion in which the last instruction executed in the method is the recursive call. As long as the recursive call is in tail position, Scala detects and compiles it to the same sort of bytecode as would be emitted for a while loop

```scala mdoc
def factorial(n: Int): Int = {
  @scala.annotation.tailrec
  def loop(index: Int, result: Int): Int = index match {
    case i if i == 0 =>
      loop(1, 1 * result)
    case i if i < n =>
      loop(i + 1, i * result)
    case i =>
      i * result
  }
  loop(0, 1)
}
```

### *What is a function literal?*

**Function literal** is a synonyms for **anonymous function**. Because functions are just ordinary Scala objects, we say that they are **first-class values**. A function literal is syntactic sugar for an object with a method called apply

```scala mdoc
val lessThan0 = (a: Int, b: Int) => a < b
val lessThan1: (Int, Int) => Boolean = (a, b) => a < b
val lessThan2 = new Function2[Int, Int, Boolean] {
  override def apply(a: Int, b: Int): Boolean = a < b
}
```

## More resources

* [Constraints Liberate, Liberties Constrain](https://www.youtube.com/watch?v=GqmsQeSzMdw) by Runar Bjarnason (Video)
