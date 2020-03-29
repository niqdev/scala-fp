package com.github.niqdev.http4s

import java.nio.charset.StandardCharsets

import cats.effect.IO
import org.http4s.{ Method, Request, Response, Status, Uri }
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class HttpServiceSpec extends AnyWordSpecLike with Matchers {

  "HttpService" should {

    "verify endpoints" in {
      val request: Request[IO] = Request[IO](Method.GET, Uri.unsafeFromString("/hello/scala"))

      val response: Response[IO] = HttpService[IO].endpoints.run(request).unsafeRunSync()

      response.status shouldBe Status.Ok
      val byteArray = response.body.compile.toVector.unsafeRunSync().toArray
      new String(byteArray, StandardCharsets.UTF_8) shouldBe "Hello scala!"
    }
  }

}
