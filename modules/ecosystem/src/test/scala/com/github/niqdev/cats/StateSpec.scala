package com.github.niqdev.cats

import org.scalatest.{Matchers, WordSpecLike}

final class StateSpec extends WordSpecLike with Matchers {

  "State" should {

    "verify examples" in {
      import cats.data.State

      /*
       * (1) transforms an input state to an output state
       * (2) computes a result
       *
       * run: provide initial state
       */
      val myState = State[Int, String] { state =>
        (state, s"state=$state")
      }

      // state and result
      myState.run(42).value shouldBe Tuple2(42, "state=42")
      // only state
      myState.runS(42).value shouldBe 42
      // only result
      myState.runA(42).value shouldBe "state=42"
    }
  }

}
