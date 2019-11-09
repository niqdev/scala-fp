---
id: fp-intro
title: Introduction
---

## Resources

* [Functional Programming in Scala](https://amzn.to/2OCFpQG) (2014) by Paul Chiusano and Runar Bjarnason (Book)
* [Functional Programming, Simplified](https://amzn.to/2OCFROS) (2017) by Alvin Alexander (Book)
* [Why Functional Programming Matters](https://www.cs.kent.ac.uk/people/staff/dat/miranda/whyfp90.pdf) (Paper)
* [Functional Programming Basics](https://pragprog.com/magazines/2013-01/functional-programming-basics)
* [Functional Programming For The Rest of Us](http://www.defmacro.org/2006/06/19/fp.html)
* [The Downfall of Imperative Programming](https://www.fpcomplete.com/blog/2012/04/the-downfall-of-imperative-programming)
* [Parallelism and concurrency need different tools](http://yosefk.com/blog/parallelism-and-concurrency-need-different-tools.html)
* [Constraints Liberate, Liberties Constrain](https://www.youtube.com/watch?v=GqmsQeSzMdw) by Runar Bjarnason (Video)
* [FP to the Max](https://youtu.be/sxudIMiOo68) (Video)

## FAQ

### *What is the uniform access principal?*

The *uniform access principle* states that variables, precomputed properties and parameterless functions should be accessed using the same syntax. Therefore not betraying whether they are implemented through storage or through computation.

Scala supports this principle by not allowing parentheses to be placed at call sites of parameterless functions. A parameterless function definition `def` can be changed to a `val` or vice versa, without affecting client code

### *What referentially transparent means?*

An expression `e` is *referentially transparent* if, for all programs `p`, all occurrences of `e` in `p` can be replaced by the result of evaluating `e` without affecting the meaning of `p`

* [Referential Transparency](https://pierangeloc.github.io/blog/2018/06/01/on-RT-and-FP)

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

### *What is an Algebraic Data Type?*

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

* [Algebraic Data Types in Scala](https://alvinalexander.com/scala/fp-book/algebraic-data-types-adts-in-scala)
* [What the Heck are Algebraic Data Types?](https://merrigrove.blogspot.com/2011/12/another-introduction-to-algebraic-data.html)

### *How for-comprehensions is desugared?*

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

* [For Comprehensions](https://docs.scala-lang.org/tour/for-comprehensions.html) (Documentation)

### *What is a Type Class?*

A *Type Class* is a programming pattern that allow to extend existing libraries with new functionality, without using traditional inheritance and without altering the original library source code using a combination of ad-hoc polymorphism, parametric polymorphism (type parameters) and implicits

* [Type Classes as Objects and Implicits](http://ropas.snu.ac.kr/~bruno/papers/TypeClasses.pdf) (Paper)
* [Type classes in Scala](https://blog.scalac.io/2017/04/19/typeclasses-in-scala.html)
* [Returning the "Current" Type in Scala](http://tpolecat.github.io/2015/04/29/f-bounds.html)
* [Typeclass 101: ad hoc polymorphism in scala](https://julien-truffaut.github.io/Typeclass)
* [All you don't need to know about Typeclasses](http://workday.github.io/assets/scala-exchange-type-classes)
* [A Small Example of the Typeclass Pattern in Scala](https://web.archive.org/web/20171223221256/http://www.casualmiracles.com/2012/05/03/a-small-example-of-the-typeclass-pattern-in-scala)
* [Typeclasses 101](http://learnyouahaskell.com/types-and-typeclasses#typeclasses-101)
* [Scala/Haskell: A simple example of type classes](https://markhneedham.com/blog/2012/05/22/scalahaskell-a-simple-example-of-type-classes)
