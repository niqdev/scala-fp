package com.github.niqdev.pipeline.opensearch

import cats.effect.{ IO, Resource, ResourceApp }
import org.slf4j.LoggerFactory

// sbt "pipeline/runMain com.github.niqdev.pipeline.opensearch.OpenSearchExample"
object OpenSearchExample extends ResourceApp.Simple {

  private val logger = LoggerFactory.getLogger(OpenSearchExample.getClass.getSimpleName)

  val run: Resource[IO, Unit] =
    for {
      _      <- Resource.pure(IO.pure(logger.info("OpenSearch example")))
      client <- OpenSearchClient.make[IO]
      info   <- client.info().toResource
      _      <- IO.pure(logger.info(s"cluster version: ${info.version}")).toResource
    } yield ()
}
