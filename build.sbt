lazy val info = new {
  val organization = "com.github.niqdev"
  val name         = "scala-fp"
  val scalaVersion = "2.13.3"
}

lazy val versions = new {
  // ecosystem
  val shapeless      = "2.3.3"
  val catsCore       = "2.1.1"
  val catsEffect     = "2.1.4"
  val catsRetry      = "1.1.1"
  val log4cats       = "1.1.1"
  val zio            = "1.0.0-RC21-2"
  val zioInteropCats = "2.1.3.0-RC16"
  val circe          = "0.13.0"
  val newtype        = "0.4.4"
  val refined        = "0.9.15"
  val squants        = "1.6.0"
  val enumeratum     = "1.6.1"
  val fs2            = "2.4.2"
  val http4s         = "0.21.6"
  val doobie         = "0.9.0"
  val flyway         = "6.5.2"
  val caliban        = "0.9.0"
  val magnolia       = "0.16.0"
  val droste         = "0.8.0"
  val logback        = "1.2.3"

  // test
  val scalatest     = "3.2.0"
  val scalatestplus = "3.2.0.0"
  val scalacheck    = "1.14.3"

  // common
  val kindProjector = "0.11.0"
}

lazy val dependencies = new {
  lazy val ecosystem = Seq(
    "com.chuusai"           %% "shapeless"                 % versions.shapeless,
    "org.typelevel"         %% "cats-core"                 % versions.catsCore,
    "org.typelevel"         %% "cats-effect"               % versions.catsEffect,
    "com.github.cb372"      %% "cats-retry"                % versions.catsRetry,
    "io.chrisdavenport"     %% "log4cats-core"             % versions.log4cats,
    "io.chrisdavenport"     %% "log4cats-slf4j"            % versions.log4cats,
    "dev.zio"               %% "zio"                       % versions.zio,
    "dev.zio"               %% "zio-streams"               % versions.zio,
    "dev.zio"               %% "zio-interop-cats"          % versions.zioInteropCats,
    "io.circe"              %% "circe-core"                % versions.circe,
    "io.circe"              %% "circe-generic"             % versions.circe,
    "io.circe"              %% "circe-parser"              % versions.circe,
    "io.circe"              %% "circe-refined"             % versions.circe,
    "io.estatico"           %% "newtype"                   % versions.newtype,
    "eu.timepit"            %% "refined"                   % versions.refined,
    "org.typelevel"         %% "squants"                   % versions.squants,
    "com.beachape"          %% "enumeratum"                % versions.enumeratum,
    "com.beachape"          %% "enumeratum-circe"          % versions.enumeratum,
    "com.beachape"          %% "enumeratum-doobie"         % "1.6.0",
    "co.fs2"                %% "fs2-core"                  % versions.fs2,
    "co.fs2"                %% "fs2-io"                    % versions.fs2,
    "org.http4s"            %% "http4s-dsl"                % versions.http4s,
    "org.http4s"            %% "http4s-blaze-server"       % versions.http4s,
    "org.http4s"            %% "http4s-blaze-client"       % versions.http4s,
    "org.http4s"            %% "http4s-prometheus-metrics" % versions.http4s,
    "org.tpolecat"          %% "doobie-core"               % versions.doobie,
    "org.tpolecat"          %% "doobie-refined"            % versions.doobie,
    "org.tpolecat"          %% "doobie-h2"                 % versions.doobie,
    "org.flywaydb"          % "flyway-core"                % versions.flyway,
    "com.github.ghostdogpr" %% "caliban"                   % versions.caliban,
    "com.github.ghostdogpr" %% "caliban-http4s"            % versions.caliban,
    "com.github.ghostdogpr" %% "caliban-cats"              % versions.caliban,
    "com.propensive"        %% "magnolia"                  % versions.magnolia,
    "io.higherkindness"     %% "droste-core"               % versions.droste,
    "ch.qos.logback"        % "logback-classic"            % versions.logback
  )

  lazy val test = Seq(
    "org.scalatest"     %% "scalatest"             % versions.scalatest     % Test,
    "org.tpolecat"      %% "doobie-scalatest"      % versions.doobie        % Test,
    "org.scalatestplus" %% "scalacheck-1-14"       % versions.scalatestplus % Test,
    "org.scalacheck"    %% "scalacheck"            % versions.scalacheck    % Test,
    "com.beachape"      %% "enumeratum-scalacheck" % versions.enumeratum    % Test
  )
}

lazy val commonSettings = Seq(
  organization := info.organization,
  scalaVersion := info.scalaVersion,
  // https://docs.scala-lang.org/overviews/compiler-options/index.html
  // https://tpolecat.github.io/2017/04/25/scalac-flags.html
  // https://github.com/DavidGregory084/sbt-tpolecat
  // https://nathankleyn.com/2019/05/13/recommended-scalac-flags-for-2-13
  // http://eed3si9n.com/stricter-scala-with-xlint-xfatal-warnings-and-scalafix
  // https://alexn.org/blog/2020/05/26/scala-fatal-warnings.html
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8", // source files are in UTF-8
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-explaintypes", // explain type errors in more detail
    "-language:existentials", // existential types (besides wildcard types) can be written and inferred
    "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
    "-language:implicitConversions", // allow definition of implicit functions called views
    "-Xlint", // enable handy linter warnings
    "-Wconf:any:error", // configurable warnings see https://github.com/scala/scala/pull/8373
    "-Ymacro-annotations", // required by newtype
    "-Xsource:2.13"
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % versions.kindProjector cross CrossVersion.full)
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
