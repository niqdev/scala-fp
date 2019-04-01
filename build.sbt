lazy val root = (project in file("."))
  .enablePlugins(MicrositesPlugin)
  .settings(
    organization := "com.github.niqdev",
    name := "fp",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",

    // https://docs.scala-lang.org/overviews/compiler-options/index.html
    scalacOptions ++= Seq(
      "-encoding", "utf8",
      "-Xfatal-warnings",
      "-deprecation",
      "-unchecked",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials",
      "-language:postfixOps"
    )
  )