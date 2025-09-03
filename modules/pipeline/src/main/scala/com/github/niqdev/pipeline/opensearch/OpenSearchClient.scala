package com.github.niqdev.pipeline.opensearch

import cats.effect.{ Async, Resource }
import cats.syntax.functor._
import org.apache.hc.core5.http.HttpHost
import org.opensearch.client.json.jackson.JacksonJsonpMapper
import org.opensearch.client.opensearch.OpenSearchAsyncClient
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder

final case class OpenSearchInfo(
  clusterName: String,
  version: String
)

trait OpenSearchClient[F[_]] {
  def info(): F[OpenSearchInfo]
}

sealed class OpenSearchClientImpl[F[_]: Async](
  private val client: OpenSearchAsyncClient
) extends OpenSearchClient[F] {

  override def info(): F[OpenSearchInfo] =
    Async[F]
      .fromCompletableFuture {
        Async[F].delay(client.info())
      }
      .map { info =>
        OpenSearchInfo(
          clusterName = info.clusterName(),
          version = info.version().number()
        )
      }
}

object OpenSearchClient {

  def make[F[_]: Async]: Resource[F, OpenSearchClient[F]] = {

    val transportF = Async[F].delay {
      ApacheHttpClient5TransportBuilder
        .builder(new HttpHost("http", "localhost", 9200))
        .setMapper(new JacksonJsonpMapper())
        .build()
    }
    for {
      transport <- Resource.fromAutoCloseable(transportF)
      client = new OpenSearchAsyncClient(transport)
    } yield new OpenSearchClientImpl(client)
  }
}
