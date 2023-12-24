package com.iodesystems.rankor.app.config.sockets

import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

abstract class MappedWebSocketHandler(
  val mapping: String,
  val allowedOrigins: List<String> = listOf("*"),
) : WebSocketHandler {
  companion object {
    val LOG = LoggerFactory.getLogger(MappedWebSocketHandler::class.java)
  }

  override fun afterConnectionEstablished(session: WebSocketSession) {
    LOG.trace("afterConnectionEstablished")
  }

  override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
    LOG.trace("handleMessage")
  }

  override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
    LOG.error("handleTransportError", exception)
  }

  override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
    LOG.trace("afterConnectionClosed")
  }

  override fun supportsPartialMessages(): Boolean {
    return false
  }
}
