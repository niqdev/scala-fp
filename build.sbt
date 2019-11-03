lazy val info = new {
  val maintainer   = "niqdev"
  val organization = "com.github.niqdev"
  val name         = "scala-fp"
  val scalaVersion = "2.12.10"
}

lazy val versions = new {
  // ecosystem
  val catsCore   = "2.0.0"
  val catsEffect = "2.0.0"

  // test
  val scalatest  = "3.0.8"
  val scalacheck = "1.14.2"
}

lazy val dependencies = new {
  lazy val ecosystem = Seq(
    "org.typelevel" %% "cats-core" % versions.catsCore,
    "org.typelevel" %% "cats-effect" % versions.catsEffect
  )

  lazy val test = Seq(
    "org.scalatest"  %% "scalatest"  % versions.scalatest  % Test,
    "org.scalacheck" %% "scalacheck" % versions.scalacheck % Test
  )
}

lazy val commonSettings = Seq(
  organization := info.organization,
  scalaVersion := info.scalaVersion,

  // https://docs.scala-lang.org/overviews/compiler-options/index.html
  // https://tpolecat.github.io/2017/04/25/scalac-flags.html
  // https://github.com/DavidGregory084/sbt-tpolecat
  scalacOptions ++= Seq(
    "-encoding", "UTF-8", // source files are in UTF-8
    "-deprecation", // warn about use of deprecated APIs
    "-unchecked", // warn about unchecked type parameters
    "-feature", // warn about misused language features
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-Xlint", // enable handy linter warnings
    "-Xfatal-warnings", // turn compiler warnings into errors
    "-Ypartial-unification", // allow the compiler to unify type constructors of different arities
    "-language:postfixOps"
  )
)

lazy val basic = (project in file("modules/basic"))
  .settings(commonSettings)
  .settings(
    name := s"${info.name}-basic",
    libraryDependencies ++= dependencies.test
      .map(_.withSources)
      .map(_.withJavadoc)
  )

lazy val ecosystem = (project in file("modules/ecosystem"))
  .settings(commonSettings)
  .settings(
    name := s"${info.name}-ecosystem",
    libraryDependencies ++= (dependencies.ecosystem ++ dependencies.test)
      .map(_.withSources)
      .map(_.withJavadoc)
  )

lazy val docs = (project in file("modules/docs"))
  .settings(commonSettings)
  .enablePlugins(MdocPlugin, DocusaurusPlugin)
  .settings(
    name := s"${info.name}-docs",

    mdocVariables := Map(
      "VERSION" -> version.value
    )
  )

lazy val fp = (project in file("modules/fp"))
  .settings(commonSettings)
  .settings(
    name := s"${info.name}-fp",
    libraryDependencies ++= dependencies.test
      .map(_.withSources)
      .map(_.withJavadoc)
  )

lazy val root = project
  .in(file("."))
  .aggregate(basic, ecosystem, fp, docs)
  .settings(
    name := info.name
  )
