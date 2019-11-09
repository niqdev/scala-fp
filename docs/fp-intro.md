---
id: fp-intro
title: Functional Programming
---

## Resources

* [Constraints Liberate, Liberties Constrain](https://www.youtube.com/watch?v=GqmsQeSzMdw) by Runar Bjarnason (Video)

## Basic concepts

### *What is the uniform access principal?*

The uniform access principle states that variables, precomputed properties and parameterless functions should be accessed using the same syntax. Therefore not betraying whether they are implemented through storage or through computation. Scala supports this principle by not allowing parentheses to be placed at call sites of parameterless functions. A parameterless function definition `def` can be changed to a `val` or vice versa, without affecting client code

### *What referentially transparent means?*

An expression `e` is **referentially transparent** if, for all programs `p`, all occurrences of `e` in `p` can be replaced by the result of evaluating `e` without affecting the meaning of `p`

* [Referential Transparency](https://pierangeloc.github.io/blog/2018/06/01/on-RT-and-FP) (Blog)

### *What is a pure function?*

A function `f` is **pure** if the expression `f(x)` is referentially transparent for all referentially transparent `x`. Hence a pure function is **modular** and **composable**

### *What is a higher-order function?*

A **higher-order function** is a function that takes other functions as arguments or returns a function as result

### *What is a recursive function?*

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
