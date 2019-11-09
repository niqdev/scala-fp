---
id: fp-intro
title: Introduction
---

## FAQ

### *What are some of the benefits of Functional Programming?*

* [Why Functional Programming Matters](https://www.cs.kent.ac.uk/people/staff/dat/miranda/whyfp90.pdf) (Paper)
* [Functional Programming Basics](https://pragprog.com/magazines/2013-01/functional-programming-basics)
* [Functional Programming For The Rest of Us](http://www.defmacro.org/2006/06/19/fp.html)
* [The Downfall of Imperative Programming](https://www.fpcomplete.com/blog/2012/04/the-downfall-of-imperative-programming)
* [Parallelism and concurrency need different tools](http://yosefk.com/blog/parallelism-and-concurrency-need-different-tools.html)
* [Benefits of Functional Programming](https://alvinalexander.com/scala/fp-book/benefits-of-functional-programming)

TL;DR

* Pure functions are easier to reason about
* Function signatures are more meaningful
* Parallel/Concurrent programming is easier
* Testing is easier and pure functions lend themselves well to techniques like property-based testing

### *What is the uniform access principal?*

The *uniform access principle* states that variables, precomputed properties and parameterless functions should be accessed using the same syntax. Therefore not betraying whether they are implemented through storage or through computation.

Scala supports this principle by not allowing parentheses to be placed at call sites of parameterless functions. A parameterless function definition `def` can be changed to a `val` or vice versa, without affecting client code

### *What referentially transparent means?*

* [Referential Transparency](https://pierangeloc.github.io/blog/2018/06/01/on-RT-and-FP)

An expression `e` is *referentially transparent* if, for all programs `p`, all occurrences of `e` in `p` can be replaced by the result of evaluating `e` without affecting the meaning of `p`

### *What is a pure function?*

A function `f` is *pure* if the expression `f(x)` is referentially transparent for all referentially transparent `x`. Hence a pure function is **modular** and **composable**

### *What is a higher-order function?*

A *higher-order function* is a function that takes other functions as arguments or returns a function as result

### *What is a recursive function?*

A *recursive function* is a function which calls itself. With *head recursion*, the recursive call is not the last instruction in the function

A *tail recursive function* is a special case of recursion in which the last instruction executed in the method is the recursive call. As long as the recursive call is in tail position, Scala detects and compiles it to the same sort of bytecode as would be emitted for a while loop

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

*Function literal* is a synonyms for **anonymous function**. Because functions are just ordinary Scala objects, we say that they are **first-class values**. A function literal is syntactic sugar for an object with a method called apply

```scala mdoc
val lessThan0 = (a: Int, b: Int) => a < b
val lessThan1: (Int, Int) => Boolean = (a, b) => a < b
val lessThan2 = new Function2[Int, Int, Boolean] {
  override def apply(a: Int, b: Int): Boolean = a < b
}
```

### *How for-comprehensions is desugared?*

* [For Comprehensions](https://docs.scala-lang.org/tour/for-comprehensions.html) (Documentation)

```scala mdoc
// (1) works because "foreach" is defined
for (i <- List(1, 2, 3)) println(i)

// (2) "yield" works because "map" is defined
for (i <- List(1, 2, 3)) yield i * 2

// (3) "if" works because "withFilter" is defined
for (i <- List(1, 2, 3, 4); if i % 2 == 0) yield i*2

// (4) works because "flatMap" is defined
for (i <- List(1, 2, 3, 4); j <- List(3, 4, 5, 6); if i == j) yield i
```

### *What is an Algebraic Data Type?*

* [Algebraic Data Types in Scala](https://alvinalexander.com/scala/fp-book/algebraic-data-types-adts-in-scala)
* [What the Heck are Algebraic Data Types?](https://merrigrove.blogspot.com/2011/12/another-introduction-to-algebraic-data.html)

In type theory, regular data structures can be described in terms of sums, products and recursive types. This leads to an algebra for describing data structures (and so-called algebraic data types). Such data types are common in statically typed functional languages

An *algebraic data type* (ADT) is just a data type defined by one or more data constructors, each of which may contain zero or more arguments. The data type is the **sum or union** of its data constructors, and each data constructor is the **product** of its arguments, hence the name algebraic data type

**Example**

* these types represent a **sum** type because `Shape` is a `Circle` ***or*** a `Rectangle`
* `Circle` is a **product** type because it has a radius
* `Rectangle` is a **product** type because it has a width ***and*** a height

```scala
sealed trait Shape
final case class Circle(radius: Double) extends Shape
final case class Rectangle(width: Double, height: Double) extends Shape
```

Sum types and product types provide the necessary abstraction for structuring various data of a domain model. Whereas sum types let model the variations within a particular data type, product types help cluster related data into a larger abstraction

An API should form an algebra — that is, a collection of data types, functions over these data types, and importantly, laws or properties that express relationships between these functions

### *What are inhabitants of a type?*

Inhabitants of a type are values for that types. Algebraic Data Types can be thought of in terms of regular algebraic equations and its result gives the number of inhabitants

* sum types: `Either[A, B]` or `A or B` corresponds to the equation `A + B`
* products types: `(A, B)` or `Tuple2` or `A and B` corresponds to the equation `A * B`
* exponentiation: `A -> B` or `Function1` corresponds to the equation `B ^ A`
    * e.g. `Boolean -> Boolean` is `2 ^ 2`
* the `Unit` data type corresponds to the value `1`
* the `Void` data type corresponds to the value `0`

### *What is an effectful computation?*

In functional programming, an effect adds some capabilities to a computation. An effect is modeled usually in the form of a **type constructor** that constructs types with these additional capabilities

* `List[A]` adds the effect of aggregation on A
* `Option[A]` adds the capability of optionality for the type A
* `Try[A]` models the effects of exceptions
