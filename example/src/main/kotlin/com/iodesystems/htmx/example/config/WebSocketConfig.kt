package com.iodesystems.htmx.example.config

import com.iodesystems.rankor.app.config.sockets.MappedWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
open class WebSocketConfig(
  val handlers: List<MappedWebSocketHandler>,
) : WebSocketConfigurer {


  override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    handlers.forEach { handler ->
      registry.addHandler(handler, handler.mapping).setAllowedOrigins(*handler.allowedOrigins.toTypedArray())
    }
  }
}
