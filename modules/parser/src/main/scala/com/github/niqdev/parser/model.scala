package com.github.niqdev.parser

import monix.newtypes.NewtypeWrapped

// https://httpd.apache.org/docs/current/logs.html#accesslog
case class LogExample(
  dateTime: java.time.OffsetDateTime,
  method: Http.Method
  //  path: Http.Path,
  //  statusCode: Http.StatusCode,
  //  time: Http.Time,
  //  contentLength: Long,
  //  ipAddress: Http.IpAddress,
  //  trackingId: java.util.UUID
)

object Http {

  object Method extends NewtypeWrapped[String]
  type Method = Method.Type

  object Path extends NewtypeWrapped[String]
  type Path = Path.Type

  object StatusCode extends NewtypeWrapped[Int]
  type StatusCode = StatusCode.Type

  object Time extends NewtypeWrapped[scala.concurrent.duration.FiniteDuration]
  type Time = Time.Type

  object IpAddress extends NewtypeWrapped[String]
  type IpAddress = IpAddress.Type
}
