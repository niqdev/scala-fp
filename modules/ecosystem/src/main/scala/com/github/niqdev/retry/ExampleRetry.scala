package com.github.niqdev.retry

import cats.effect.{ ExitCode, IO, IOApp, Sync }
import retry.RetryDetails.{ GivingUp, WillDelayAndRetry }
import retry.{ RetryDetails, RetryPolicies, retryingOnAllErrors }

import scala.concurrent.duration.FiniteDuration

object ExampleRetry extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    program.attempt.as(ExitCode.Success)

  private[this] def program: IO[String] =
    retryingOnAllErrors[String](
      policy = RetryPolicies.limitRetries[IO](5),
      onError = logError[IO]
    ) {
      IO.raiseError(new Exception("boom"))
    }

  private[this] def logError[F[_]](err: Throwable, details: RetryDetails)(implicit F: Sync[F]): F[Unit] =
    details match {
      case WillDelayAndRetry(nextDelay: FiniteDuration, retriesSoFar: Int, cumulativeDelay: FiniteDuration) =>
        F.delay(println(s"attempt: $retriesSoFar"))
      case GivingUp(totalRetries: Int, totalDelay: FiniteDuration) =>
        F.delay(println(s"max attempt exceeded $totalRetries"))
    }

}
