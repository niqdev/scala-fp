---
id: fp-ecosystem
title: Ecosystem
---

## Cats

### Resources

* [Cats](https://typelevel.org/cats) (Documentation)
* [Cats Infographic](https://github.com/tpolecat/cats-infographic)
* [Scala with Cats](https://underscore.io/books/scala-with-cats) by Noel Welsh and Dave Gurnell (Book)
* [Practical FP in Scala: A hands-on approach](https://leanpub.com/pfp-scala) by Gabriel Volpe (Book)
* **TODO** [herding cats](http://eed3si9n.com/herding-cats/index.html)
* [ScalaFP: Firsthand With Scala-Cats](https://www.signifytechnology.com/blog/2018/07/scalafp-firsthand-with-scala-cats-monads-number-1-by-harmeet-singh)
* [Scala Cats library for dummies](https://medium.com/@abu_nadhr/scala-cats-library-for-dummies-part-1-8ec47af7a144)
* [A comprehensive introduction to Cats-mtl](https://typelevel.org/blog/2018/10/06/intro-to-mtl.html)
* [Law Enforcement using Discipline](https://typelevel.org/blog/2013/11/17/discipline.html)

## ZIO

### Resources

* [ZIO](https://zio.dev) (Documentation)
* [Reimagining Functional Type Classes](https://www.youtube.com/watch?v=OwmHgL9F_9Q) (video)
* [An intro to writing native DB driver in Scala](https://kadek-marek.medium.com/a-guide-to-writing-native-db-driver-in-scala-7f5cb1cf20a0)

### Examples

```bash
# run
sbt "ecosystem/runMain com.github.niqdev.zio.ExampleZIOApp"
sbt "ecosystem/runMain com.github.niqdev.zio.ExampleZLayerApp"
```

## http4s

### Resources

* [http4s](https://http4s.org) (Documentation)
* [Pure functional HTTP APIs in Scala](https://leanpub.com/pfhais) by Jens Grassel (Book)

### Examples

> TODO examples

* JSON encoder/decoder with circe
* integration with doobie

```bash
# run tests
sbt "test:testOnly *http4s*"

# start server
sbt "ecosystem/runMain com.github.niqdev.http4s.ExampleServer"

# verify endpoints
http :8080/hello/scala
http :8080/metrics
```

## FS2

### Resources

* [FS2](https://fs2.io) (Documentation)

### Examples

```bash
# run tests
sbt "test:testOnly *fs2*"
```

## Doobie

### Resources

* [doobie](https://tpolecat.github.io/doobie) (Documentation)
* [Pure Functional Database Programming with Fixpoint Types](https://www.youtube.com/watch?v=7xSfLPD6tiQ) by Rob Norris (video)
* [Describing Data with free applicative functors](https://www.youtube.com/watch?v=oRLkb6mqvVM) by Kris Nuttycombe (video)

### Examples

```bash
# run
sbt "ecosystem/runMain com.github.niqdev.doobie.ExampleH2"
```

## shapeless

### Resources

* [shapeless](https://github.com/milessabin/shapeless/wiki) (Documentation)
* [The Type Astronaut's Guide to Shapeless](https://underscore.io/books/shapeless-guide) by Dave Gurnell (Book)
* [Shapeless for Mortals](http://fommil.com/scalax15) (2015) by Sam Halliday (Talk)
* [First-class polymorphic function values in shapeless](https://milessabin.com/blog/2012/04/27/shapeless-polymorphic-function-values-1)
* [Scala Tagged types](http://www.vlachjosef.com/tagged-types-introduction)
* [Scala's Modular Roots](http://lambdafoo.com/scala-syd-2015-modules)
* [Type classes and generic derivation](https://meta.plasm.us/posts/2015/11/08/type-classes-and-generic-derivation)
* [Automatic type-class derivation with Shapeless](https://www.lyh.me/automatic-type-class-derivation-with-shapeless.html)

### Examples

```bash
# run tests
sbt "test:testOnly *shapeless*"
```

## ScalaTest and ScalaCheck

### Resources

* [ScalaTest](http://www.scalatest.org) (Documentation)
* [ScalaCheck](https://www.scalacheck.org) (Documentation)
* [ScalaCheck User Guide](https://github.com/rickynils/scalacheck/blob/master/doc/UserGuide.md)
* [Practical ScalaCheck](http://noelmarkham.github.io/practical-scalacheck/index.html#/)
* [ScalaCheck lesson](https://github.com/alvinj/FPScalaCheck)
* [Code Examples](https://booksites.artima.com/scalacheck/examples/index.html)
* [Using ScalaCheck to Verify Infinite Algebraic Structures](http://jtfmumm.com/blog/2015/09/04/adventures-in-abstract-algebra-part-4-using-scalacheck-to-verify-infinite-algebraic-structures/)

### Examples

```bash
# run tests
sbt "test:testOnly *scalacheck*"

# auto derivation of ScalaCheck generators using Magnolia
sbt "ecosystem/test:runMain com.github.niqdev.scalacheck.RandomApp"
```

## GraphQL

### Resources

* [GraphQL](https://graphql.org) (Documentation)
* [Specification](http://spec.graphql.org)
* [GraphQL Playground](https://www.graphqlbin.com)
* [The Fullstack Tutorial for GraphQL](https://www.howtographql.com)
* [Sangria](https://sangria-graphql.org)
* [Building GraphQL API with Sangria](https://www.youtube.com/watch?v=ymILgZAdfnA) by Oleg Ilyenko (Video)
* [GraphQL at Twitter](https://about.sourcegraph.com/graphql/graphql-at-twitter)
* [Caliban](https://ghostdogpr.github.io/caliban)
* [Caliban: Designing a Functional GraphQL Library](https://www.youtube.com/watch?v=OC8PbviYUlQ) by Pierre Ricadat (Video)
* [GraphQL in Scala with Caliban](https://medium.com/@ghostdogpr/graphql-in-scala-with-caliban-part-1-8ceb6099c3c2)
* [GraphiQL](https://github.com/graphql/graphiql)
    - [graphiql-app](https://github.com/skevy/graphiql-app) (GUI)
    - [graphiql](https://github.com/friendsofgo/graphiql) (Docker)

### Examples

```bash
# run
sbt "ecosystem/runMain com.github.niqdev.caliban.CalibanZIOApp"
sbt "ecosystem/runMain com.github.niqdev.caliban.CalibanCatsHttp4sApp"

# verify
http -v :8080/api/graphql query='{models{id}model(id:"model-8"){description,count,valid}}'

# run tests
sbt "test:testOnly *caliban*"
```

## Droste

### Resources

* [Droste](https://github.com/higherkindness/droste) (Documentation)
* [Recursion schemes fundamentals](https://www.47deg.com/blog/recursion-schemes-introduction)
* Practical Droste [ [code](https://github.com/BeniVF/practical-droste) | [video](https://www.youtube.com/watch?v=YBEc0Upntjg) ] by Beni Villa
* Recursion schemes with Higherkindness [ [code](https://github.com/higherkindness/ersatz) | [video](https://www.youtube.com/watch?v=tP77Ryy9Qxs) ] by Oli Makhasoeva & Andy Scott
* [Peeling the Banana: Recursion Schemes from First Principles](https://www.youtube.com/watch?v=XZ9nPZbaYfE) by Zainab Ali (video)
* [Recursion Schemes in Scala](https://free.cofree.io/2017/11/13/recursion)
* Matryoshka
    - [Matryoshka](https://github.com/precog/matryoshka) (Documentation)
    - [Introduction to Recursion Schemes with Matryoshka](https://akmetiuk.com/posts/2017-03-10-matryoshka-intro.html)
    - [AST playground: recursion schemes and recursive data](https://kubuszok.com/2019/ast-playground-recursion-schemes-and-recursive-data)
    - [matryoshka-examples-scala](https://github.com/LoyolaChicagoCode/matryoshka-examples-scala)
    - [Recursion Schemes Cookbook](https://github.com/vil1/recursion-schemes-cookbook)

### Examples

```bash
# run tests
sbt "test:testOnly *droste*"
```

## cats-retry

### Resources

* [cats-retry](https://cb372.github.io/cats-retry/docs) (Documentation)

### Examples

```bash
# run
sbt "ecosystem/runMain com.github.niqdev.retry.ExampleRetry"
```

## More libraries

* [refined](https://github.com/fthomas/refined)
* [Enumeratum](https://github.com/lloydmeta/enumeratum)
* [Monocle](http://julien-truffaut.github.io/Monocle)
    * [Lens in scala](http://koff.io/posts/292173-lens-in-scala)
    * [Example](https://github.com/jdegoes/lambdaconf-2014-introgame#a-simple-lens)
* [circe](https://circe.github.io/circe)
* [Ciris](https://cir.is)
