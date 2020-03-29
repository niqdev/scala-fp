lazy val info = new {
  val maintainer   = "niqdev"
  val organization = "com.github.niqdev"
  val name         = "scala-fp"
  val scalaVersion = "2.13.1"
}

lazy val versions = new {
  // ecosystem
  val shapeless      = "2.3.3"
  val catsCore       = "2.1.1"
  val catsEffect     = "2.1.2"
  val catsRetry      = "1.1.0"
  val zio            = "1.0.0-RC18-2"
  val zioInteropCats = "2.0.0.0-RC12"
  val refined        = "0.9.13"
  val fs2            = "2.3.0"
  val http4s         = "0.21.1"
  val doobie         = "0.8.8"
  val caliban        = "0.7.3"
  val logback        = "1.2.3"

  // test
  val scalatest  = "3.1.1"
  val scalacheck = "1.14.3"

  // common
  val kindProjector = "0.11.0"
  val silencer      = "1.6.0"
}

lazy val dependencies = new {
  lazy val ecosystem = Seq(
    "com.chuusai"           %% "shapeless"                 % versions.shapeless,
    "org.typelevel"         %% "cats-core"                 % versions.catsCore,
    "org.typelevel"         %% "cats-effect"               % versions.catsEffect,
    "com.github.cb372"      %% "cats-retry"                % versions.catsRetry,
    "dev.zio"               %% "zio"                       % versions.zio,
    "dev.zio"               %% "zio-streams"               % versions.zio,
    "dev.zio"               %% "zio-interop-cats"          % versions.zioInteropCats,
    "eu.timepit"            %% "refined"                   % versions.refined,
    "co.fs2"                %% "fs2-core"                  % versions.fs2,
    "co.fs2"                %% "fs2-io"                    % versions.fs2,
    "org.http4s"            %% "http4s-dsl"                % versions.http4s,
    "org.http4s"            %% "http4s-blaze-server"       % versions.http4s,
    "org.http4s"            %% "http4s-blaze-client"       % versions.http4s,
    "org.http4s"            %% "http4s-prometheus-metrics" % versions.http4s,
    "org.tpolecat"          %% "doobie-core"               % versions.doobie,
    "org.tpolecat"          %% "doobie-h2"                 % versions.doobie,
    "com.github.ghostdogpr" %% "caliban"                   % versions.caliban,
    "com.github.ghostdogpr" %% "caliban-http4s"            % versions.caliban,
    "com.github.ghostdogpr" %% "caliban-cats"              % versions.caliban,
    "ch.qos.logback"        % "logback-classic"            % versions.logback
  )

  lazy val test = Seq(
    "org.scalatest"  %% "scalatest"        % versions.scalatest  % Test,
    "org.tpolecat"   %% "doobie-scalatest" % versions.doobie     % Test,
    "org.scalacheck" %% "scalacheck"       % versions.scalacheck % Test
  )
}

lazy val commonSettings = Seq(
  organization := info.organization,
  scalaVersion := info.scalaVersion,
  // https://docs.scala-lang.org/overviews/compiler-options/index.html
  // https://tpolecat.github.io/2017/04/25/scalac-flags.html
  // https://github.com/DavidGregory084/sbt-tpolecat
  // https://nathankleyn.com/2019/05/13/recommended-scalac-flags-for-2-13
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8", // source files are in UTF-8
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-deprecation", // warn about use of deprecated APIs
    "-unchecked", // warn about unchecked type parameters
    "-feature", // warn about misused language features
    "-explaintypes", // explain type errors in more detail
    "-language:existentials", // existential types (besides wildcard types) can be written and inferred
    "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
    "-language:implicitConversions", // allow definition of implicit functions called views
    "-Xlint", // enable handy linter warnings
    "-Xfatal-warnings", // turn compiler warnings into errors
    "-Ywarn-unused:imports", // warn if an import selector is not referenced
    "-Xsource:2.13"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases")
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % versions.kindProjector cross CrossVersion.full),
  libraryDependencies ++= Seq(
    compilerPlugin("com.github.ghik" % "silencer-plugin" % versions.silencer cross CrossVersion.full),
    "com.github.ghik" % "silencer-lib" % versions.silencer % Provided cross CrossVersion.full
  )
)

lazy val common = (project in file("modules/common"))
  .settings(commonSettings)
  .settings(
    name := s"${info.name}-common",
    libraryDependencies ++= dependencies.test
      .map(_.withSources)
      .map(_.withJavadoc)
  )

lazy val fp = (project in file("modules/fp"))
  .dependsOn(common)
  .settings(commonSettings)
  .settings(
    name := s"${info.name}-fp",
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

lazy val root = project
  .in(file("."))
  .aggregate(common, fp, ecosystem, docs)
  .settings(
    name := info.name,
    addCommandAlias("checkFormat", ";scalafmtCheckAll;scalafmtSbtCheck"),
    addCommandAlias("format", ";scalafmtAll;scalafmtSbt"),
    addCommandAlias("update", ";dependencyUpdates;reload plugins;dependencyUpdates;reload return"),
    addCommandAlias("build", ";checkFormat;clean;test"),
    addCommandAlias("publish", ";mdoc;docusaurusCreateSite;docusaurusPublishGhpages")
  )
