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

### *What is polymorphism?*

* [What is polymorphism?](http://eed3si9n.com/herding-cats/polymorphism.html)
* [Polymorphism, and typeclasses in Scala](http://like-a-boss.net/2013/03/29/polymorphism-and-typeclasses-in-scala.html)
* [Polymorphism in Scala](https://medium.com/@scelestino/polymorphism-in-scala-part-1-34015f9b5b13)
* [How to be polymorphic in Scala](http://scala.org.ua/presentations/scala-polymorphism)

A polymorphic type is the one whose operations can be applied to another types

In *Sub-typing Polymorphism* a type (sub-type) is related to another (super-type) in a way where operations that apply to the super-type can also apply to its sub-types i.e. when a type inherits from another

In *Parametric Polymorphism* types, methods or functions can be written generically using Type Parameters so they can handle values without depending on their types

In *Ad-hoc Polymorphism*, known as method or function overloading, the compiler applies argument of different types to functions or methods and decide which implementation is the correct to use, depending on the type of the arguments to which they are applied

### *What is the difference between monomorphic and polymorphic?*

* [Method-level parametric polymorphism](https://milessabin.com/blog/2012/04/27/shapeless-polymorphic-function-values-1/#method-level-parametric-polymorphism)

Monomorphic methods can only be applied to arguments of the fixed types specified in their signatures, whereas polymorphic methods can be applied to arguments of any types which correspond to acceptable choices for their type parameters

```scala mdoc
// monomorphic methods have type parameter-free signatures
def monomorphic(s: String): Int = s.length

// only values of type String
monomorphic("hello")

// polymorphic methods have type parameters in their signatures
def polymorphic[T](list: List[T]): Int = list.length

polymorphic(List(1, 2, 3))
polymorphic(List("foo", "bar", "baz"))
```

Only by knowing the type signature of a method is possible to infer more or less information regarding its underlying implementation

* given a monomorphic signature `List[Int] -> List[Int]`, there are too many possible implementations to say what the function does
* given a polymorphic/parametrized type signature `List[A] -> List[A]`, it's proven that all elements in the result must appear in the input if there are not side effects, which restricts the number of possible implementations

### TODO *How does the implicit resolution mechanism work?*

> TODO

* [Implicits, type classes, and extension methods](https://kubuszok.com/compiled/implicits-type-classes-and-extension-methods)
* [revisiting implicits without import tax](http://eed3si9n.com/revisiting-implicits-without-import-tax)
* [implicit parameter precedence again](http://eed3si9n.com/implicit-parameter-precedence-again)
* [Where does Scala look for implicits?](https://stackoverflow.com/questions/5598085/where-does-scala-look-for-implicits)

### *What is a type class?*

* [Type Classes as Objects and Implicits](http://ropas.snu.ac.kr/~bruno/papers/TypeClasses.pdf) (Paper)
* [Type classes in Scala](https://blog.scalac.io/2017/04/19/typeclasses-in-scala.html)
* [Returning the "Current" Type in Scala](http://tpolecat.github.io/2015/04/29/f-bounds.html)
* [Typeclass 101: ad hoc polymorphism in scala](https://julien-truffaut.github.io/Typeclass)
* [All you don't need to know about Typeclasses](http://workday.github.io/assets/scala-exchange-type-classes)
* [Scala Implicits : Type Classes Here I Come](http://debasishg.blogspot.com/2010/06/scala-implicits-type-classes-here-i.html)
* [A Small Example of the Typeclass Pattern in Scala](https://web.archive.org/web/20171223221256/http://www.casualmiracles.com/2012/05/03/a-small-example-of-the-typeclass-pattern-in-scala)
* [Typeclasses 101](http://learnyouahaskell.com/types-and-typeclasses#typeclasses-101)
* [Scala/Haskell: A simple example of type classes](https://markhneedham.com/blog/2012/05/22/scalahaskell-a-simple-example-of-type-classes)

A *Type Class* is a type system construct that supports **ad-hoc polymorphism** and is achieved by adding constraints to type variables in parametrically polymorphic types

It's a programming pattern that allow to extend existing libraries with new functionality, without using traditional inheritance and without altering the original library source code using a combination of ad-hoc polymorphism, parametric polymorphism (type parameters) and implicits

### *What are type constructors and higher-kinded types?*

* [Generics of a Higher Kind](http://adriaanm.github.io/files/higher.pdf) (Paper)
* [Fighting Bit Rot with Types](http://lampwww.epfl.ch/~odersky/papers/fsttcs2009.pdf) (Paper)
* [Kinds and some type-foo](http://eed3si9n.com/herding-cats/Kinds.html)
* [What is a higher kinded type in Scala?](https://stackoverflow.com/questions/6246719/what-is-a-higher-kinded-type-in-scala)
* [Type Constructor Polymorphism](http://adriaanm.github.io/research/2010/10/06/new-in-scala-2.8-type-constructor-inference)
* [Scala: Types of a higher kind](https://www.atlassian.com/blog/archives/scala-types-of-a-higher-kind)

![fp-hkt](assets/fp-hkt.jpg)

*Type Constructors* act like functions, but on the type level

* `42` is a proper value
* `Int` is a proper type `*`
* `def f[A](a: A) = ???` is a function that takes a type parameter `A`
    * `f` is a value constructor
    * `f` has an abstract/polymorphic type parameter `A` of first-order
    * `A` is of first-order because it cannot abstract over anything e.g. Java generics
* `List` is a type constructor
* `List[Int]` is a concrete type
* `List[+A]` is a type constructor that takes a type parameter `A`
    * `List` is a first-order-kinded type `* -> *`
* `Either` is a type constructor that takes two type parameters `A` and `B`
    * `Either` is a first-order-kinded type `* -> * -> *`
* `M` is a type constructor that takes a type constructor parameter `F`
    * `M` is a higher-kinded or higher-order type `(* -> *) -> *`

```shell
scala> :kind -v Int
Int's kind is A
*
This is a proper type.

scala> :kind -v List
List's kind is F[+A]
* -(+)-> *
This is a type constructor: a 1st-order-kinded type.

scala> :kind -v Either
Either's kind is F[+A1,+A2]
* -(+)-> * -(+)-> *
This is a type constructor: a 1st-order-kinded type.

scala> trait M[F[_]]
scala> :kind -v M
M's kind is X[F[A]]
(* -> *) -> *
This is a type constructor that takes type constructor(s): a higher-kinded type.
```

> TODO parallel between curried (function) and type constructor - see Arrows in category theory

### TODO *What is a type lambda?*

> TODO

* [Type lambdas and kind projector](https://underscore.io/blog/posts/2016/12/05/type-lambdas.html)
* [Type Lambda in Scala](http://like-a-boss.net/2014/09/27/type-lambda-in-scala.html)
* [What are type lambdas in Scala and what are their benefits?](https://stackoverflow.com/questions/8736164/what-are-type-lambdas-in-scala-and-what-are-their-benefits)
* [kind-projector](https://github.com/typelevel/kind-projector)

### *What is an effectful computation?*

In functional programming, an effect adds some capabilities to a computation. An effect is modeled usually in the form of a **type constructor** that constructs types with these additional capabilities

* `List[A]` adds the effect of aggregation on A
* `Option[A]` adds the capability of optionality for the type A
* `Try[A]` models the effects of exceptions