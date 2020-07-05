package com.github.niqdev.caliban
package pagination

import java.nio.charset.StandardCharsets
import java.util.Base64

import scala.util.Try

object utils {

  val toBase64: String => String =
    value => Base64.getEncoder.encodeToString(value.getBytes(StandardCharsets.UTF_8))

  val fromBase64: String => String =
    value => new String(Base64.getDecoder.decode(value), StandardCharsets.UTF_8)

  def removePrefix(value: String, prefixes: String*): String =
    prefixes.foldLeft(value)((v, prefix) => v.replace(prefix, ""))

  def longFromBase64(value: String, prefixes: String*): Try[Long] =
    Try(removePrefix(fromBase64(value), prefixes: _*).toLong)

}
