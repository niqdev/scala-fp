package com.github.niqdev.effect

import cats.effect.IO
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class MyResourceSpec extends AnyWordSpecLike with Matchers {

  "MyResource" should {

    "verify resource" in {
      MyResource[IO].resource.unsafeRunSync() shouldBe "[step-1|allocate][step-2|acquire][step-3|transform]"
    }
  }

}
