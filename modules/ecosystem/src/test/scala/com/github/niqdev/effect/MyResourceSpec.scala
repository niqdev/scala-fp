package com.github.niqdev.effect

import cats.effect.IO
import org.scalatest.{ Matchers, WordSpecLike }

final class MyResourceSpec extends WordSpecLike with Matchers {

  "MyResource" should {

    "verify resource" in {
      MyResource[IO].resource.unsafeRunSync() shouldBe "[step-1|allocate][step-2|acquire][step-3|transform]"
    }
  }

}
