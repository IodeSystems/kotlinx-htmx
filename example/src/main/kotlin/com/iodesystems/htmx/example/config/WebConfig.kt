package com.iodesystems.htmx.example.config

import com.iodesystems.htmx.example.HtmxHttpMessageConverter
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebConfig : WebMvcConfigurer {
  override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
    converters.add(0, HtmxHttpMessageConverter())
  }
}
