package com.iodesystems.htmx

import kotlinx.html.FlowContent
import kotlinx.html.TagConsumer
import kotlinx.html.stream.appendHTML
import java.io.ByteArrayOutputStream

data class Htmx(val block: TagConsumer<*>.() -> Unit) {
  context(FlowContent)
  fun render() = block.invoke(consumer)

  override fun toString(): String {
    val baos = ByteArrayOutputStream()
    val writer = baos.writer()
    writer.appendHTML().block()
    writer.close()
    return baos.toString()
  }

  companion object {
    fun FlowContent.hx(block: HtmxAttrs.() -> Unit) {
      block(HtmxAttrs(this))
    }
  }
}
