package com.github.niqdev.fs2

import java.nio.file.Paths

import _root_.io.circe.Json
import _root_.io.circe.fs2.stringArrayParser
import cats.effect.{ Blocker, ExitCode, IO, IOApp }
import fs2.data.json.circe._
import fs2.data.json.selector.root
import fs2.data.json.{ filter, tokens, values, wrap }
import fs2.{ Stream, io, text }

// http://web.archive.org/web/20201111215332/https://fs2.io
// https://github.com/circe/circe-fs2
// https://fs2-data.gnieh.org/documentation/json
object ExampleJson extends IOApp {

  // not used
  val copyReadme: Stream[IO, Unit] = Stream.resource(Blocker[IO]).flatMap { blocker =>
    io.file
      .readAll[IO](Paths.get("README.md"), blocker, 4096)
      .through(text.utf8Decode)
      .through(text.lines)
      .evalMap(line => IO.delay { println(s"$line"); line })
      .through(text.utf8Encode)
      .through(io.file.writeAll(Paths.get("README.md-new"), blocker))
  }

  private[this] val pathArray = Paths.get(ClassLoader.getSystemResource("fs2-example-array.json").toURI)
  val parser: Stream[IO, Json] = Stream.resource(Blocker[IO]).flatMap { blocker =>
    io.file
      .readAll[IO](pathArray, blocker, 4096)
      .through(text.utf8Decode)
      .through(stringArrayParser)
      .evalMap(line => IO.delay { println(s"$line"); line })
  }

  // https://github.com/satabin/fs2-data/blob/v0.10.0/json/jvm/src/test/scala/fs2/data/json/JsonParsertest.scala
  private[this] val pathNested = Paths.get(ClassLoader.getSystemResource("fs2-example-nested.json").toURI)
  private[this] val selector   = root.field("items").iterate.compile
  // output {}{}{}
  val jsonTokenParser: Stream[IO, Json] = Stream.resource(Blocker[IO]).flatMap { blocker =>
    io.file
      .readAll[IO](pathNested, blocker, 4096)
      .through(text.utf8Decode)
      .flatMap(Stream.emits(_))
      .through(tokens)
      .through(filter(selector))
      .through(values)
      .evalMap(json => IO.delay { println(s"$json"); json })
  }
  // output: [{},{},{}]
  // ".through(wrap.asArrayInObject(at = "events"))" output: {"events":[{},{},{}]}
  val jsonArrayParser: Stream[IO, Json] = Stream.resource(Blocker[IO]).flatMap { blocker =>
    io.file
      .readAll[IO](pathNested, blocker, 4096)
      .through(text.utf8Decode)
      .flatMap(Stream.emits(_))
      .through(tokens)
      .through(filter(selector))
      .through(wrap.asTopLevelArray)
      .through(values)
      .evalMap(json => IO.delay { println(s"$json"); json })
  }

  def run(args: List[String]): IO[ExitCode] =
    jsonArrayParser.compile.drain.as(ExitCode.Success)
}
