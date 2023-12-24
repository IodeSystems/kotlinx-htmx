package com.iodesystems.htmx.example

import com.iodesystems.htmx.Htmx
import kotlinx.html.stream.appendHTML
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter

class HtmxHttpMessageConverter : HttpMessageConverter<Htmx> {

  override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean {
    return false
  }

  override fun read(clazz: Class<out Htmx>, inputMessage: HttpInputMessage): Htmx {
    throw UnsupportedOperationException("Clients don't send html to us, c'mon!")
  }

  override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
    return Htmx::class.java.isAssignableFrom(clazz) && (
        mediaType == null
            || mediaType == MediaType.TEXT_HTML
        )
  }

  override fun getSupportedMediaTypes(): MutableList<MediaType> {
    return mutableListOf(MediaType.TEXT_HTML)
  }

  override fun write(t: Htmx, contentType: MediaType?, outputMessage: HttpOutputMessage) {
    outputMessage.headers.contentType = MediaType.TEXT_HTML
    val writer = outputMessage.body.writer()
    t.block(writer.appendHTML())
    writer.close()

  }
}
