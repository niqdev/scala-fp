---
id: introduction
title: Introduction
---

## Basic Scala concepts

### What is the Scala hierarchy?

![scala-hierarchy](assets/scala-hierarchy.png)

### What is the difference between by-name and by-value parameters?

A **by-value** parameter is evaluated before the method is invoked e.g. `(x: Int)` while a **by-name** parameter is not evaluated before the method is invoked, but each time the parameter is referenced inside the method e.g. `(x: => Int)`

### What are the differences between `def val var lazy`?

* `def` defines a method
* `val` defines a fixed value, it is immmutable and eagerly initialized
* `var` defines a variable reference, it is mutable and *it should be avoided*
* `lazy` only initialised when required and as late as possible (deferred evaluation), default is strict and it's not recomputed like by-name parameters

### What are `Nothing Nil None Empty Null null Unit`?

* `Nothing` is a trait that is the bottom subtype of every subtype of `Any`
* `Nil` is an empty list that is defined as a `List[Nothing]`
* `None` is an empty option that is defined as a `Option[Nothing]`
* `Null` is a trait and is the bottom type similar to `Nothing` but only for `AnyRef` not `AnyVal`
* `null` is an instance of the `Null` trait
* `Unit` is a subtype of `AnyVal`, it's only value is `()` and it is not represented by any object in the underlying runtime system. A method with return type `Unit` is analogous to a Java method which is declared `void`

## Basic FP concepts

### What is the uniform access principal?

The uniform access principle states that variables, precomputed properties and parameterless functions should be accessed using the same syntax. Therefore not betraying whether they are implemented through storage or through computation. Scala supports this principle by not allowing parentheses to be placed at call sites of parameterless functions. A parameterless function definition `def` can be changed to a `val` or vice versa, without affecting client code

### What referentially transparent means?

An expression `e` is **referentially transparent** if, for all programs `p`, all occurrences of `e` in `p` can be replaced by the result of evaluating `e` without affecting the meaning of `p`

### What is a pure function?

A function `f` is **pure** if the expression `f(x)` is referentially transparent for all referentially transparent `x`. Hence a pure function is **modular** and **composable**

### What is a higher-order function?

A **higher-order function** is a function that takes other functions as arguments or returns a function as result

### What is recursive function?

A **recursive function** is a function which calls itself. With **head recursion**, the recursive call is not the last instruction in the function

A **tail recursive function** is a special case of recursion in which the last instruction executed in the method is the recursive call. As long as the recursive call is in tail position, Scala detects and compiles it to the same sort of bytecode as would be emitted for a while loop

```scala mdoc
def factorial(n: Int): Int = {
  @tailrec
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
