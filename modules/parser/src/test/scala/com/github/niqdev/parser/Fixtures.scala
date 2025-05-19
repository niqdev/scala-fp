package com.github.niqdev.parser

object Fixtures {

//  val example = """2025-05-01 15:55:42        GET     /foo/status?aaa=bbb   200     0.03    3       192.168.0.1    "ca7d8158-70a9-4974-b38b-7cc512249b71" - """
  val example = """2025-05-01	15:55:42	GET"""

  private val exampleDateTime = java
    .time
    .LocalDate
    .parse("20250501", java.time.format.DateTimeFormatter.BASIC_ISO_DATE)
    .atTime(15, 55, 42)
    .atOffset(java.time.ZoneOffset.UTC)

  val exampleExpected: LogExample =
    LogExample(
      dateTime = exampleDateTime,
      method = Http.Method("GET")
    )

}
