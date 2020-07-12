package com.github.niqdev.caliban
package pagination

import java.nio.charset.StandardCharsets
import java.util.Base64

object utils {

  val toBase64: String => String =
    value => Base64.getEncoder.encodeToString(value.getBytes(StandardCharsets.UTF_8))

  val fromBase64: String => String =
    value => new String(Base64.getDecoder.decode(value), StandardCharsets.UTF_8)

  def removePrefix(value: String, prefixes: String*): String =
    prefixes.foldLeft(value)((v, prefix) => v.replace(prefix, ""))

}
