package com.iodesystems.htmx.example.controllers

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.iodesystems.htmx.Htmx
import com.iodesystems.htmx.Htmx.Companion.hx
import com.iodesystems.htmx.HtmxTrigger
import com.iodesystems.rankor.app.config.sockets.MappedWebSocketHandler
import kotlinx.html.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@RestController
@RequestMapping("/")
class ChatController(
  val chatState: ChatState,
  val objectMapper: ObjectMapper
) {

  @Service
  class ChatState {
    val messages = mutableListOf<String>()
    val sseClients = mutableListOf<WebSocketSession>()
    val wsClients = mutableListOf<SseEmitter>()
  }

  @GetMapping("/")
  fun index() = Htmx {
    html {
      header {
        script {
          src = "https://unpkg.com/htmx.org@1.9.9/dist/htmx.min.js"
        }
        script {
          src = "https://unpkg.com/htmx.org@1.9.9/dist/ext/json-enc.js"
        }
        style {
          unsafe {
            raw(
              """
              #indicator {
                  display: none;
              }
              #indicator.htmx-request{
                  display:inline;
              }
            """.trimIndent()
            )
          }
        }
      }
      body {
        hx {
          // Support json events
          json()
          // Support websockets
          ws("/chat-ws")
          // Support server sent events
          sse("/chat-sse")
        }


        div {
          id = "indicator"
          +"Loading..."
        }
        counterButton().render()
        form {
          id = "form"
          hx {
            wsSend("submit")
          }
          input {
            type = InputType.text
            name = "message"
          }
          input {
            type = InputType.submit
            value = "Send"
          }
        }
        messages().render()
        messageCount().render()
      }
    }
  }

  data class Counter(val count: Int)

  @PostMapping("/counter")
  fun counterButton(
    @RequestBody counter: Counter = Counter(0)
  ) = Htmx {
    if (counter.count > 0) {
      // Let the indicator show!
      Thread.sleep(1.seconds.toJavaDuration())
    }
    button {
      id = "button"
      hx {
        post("/counter")
        swap()
        indicator("#indicator")
        trigger {
          click()
          queue(HtmxTrigger.Queue.first)
          vals(objectMapper.writeValueAsString(counter.copy(count = counter.count + 1)))
        }
      }
      +"Click Count ${counter.count}"
    }
  }

  fun messages(loadedWithWs: Boolean = false) = Htmx {
    div {
      id = "messages"
      if (loadedWithWs) {
        div {
          +"Loaded with WS:"
        }
      }
      chatState.messages.forEach {
        div {
          +it
        }
      }
    }
  }

  fun messageCount(loadedWithSse: Boolean = false) = Htmx {
    div {
      hx {
        sseSwap("message")
      }
      id = "message-count"
      if (loadedWithSse) {
        div {
          +"Loaded with SSE:"
        }
      }
      +"${chatState.messages.size} messages"
    }
  }

  @GetMapping("/chat-sse")
  fun chatSse(): SseEmitter {
    val emitter = SseEmitter(Long.MAX_VALUE)
    chatState.wsClients.add(emitter)
    emitter.onCompletion {
      chatState.wsClients.remove(emitter)
    }
    return emitter
  }

  @Service
  class ChatWebSocketHandler(
    val objectMapper: ObjectMapper,
    val chatState: ChatState
  ) : MappedWebSocketHandler("/chat-ws") {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Message(val message: String)

    // Late init because of circular dependency
    // We want to re-use the controller to build responses
    @Autowired
    lateinit var chatController: ChatController

    override fun afterConnectionEstablished(session: WebSocketSession) {
      chatState.sseClients.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
      chatState.sseClients.remove(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
      // Parse and apply state
      val parsed = objectMapper.readValue(message.payload as String, Message::class.java)
      chatState.messages.add(parsed.message)

      // Build responses
      val wsMessage = chatController.messages(true).toString()
      val sseMessage = SseEmitter.event()
        .name("message")
        .data(chatController.messageCount(true).toString())

      // Send to ws clients
      val failed = mutableListOf<SseEmitter>()
      chatState.wsClients.forEach {
        try {
          it.send(sseMessage)
        } catch (e: IOException) {
          failed.add(it)
        }
      }
      failed.forEach {
        chatState.wsClients.remove(it)
      }

      // Send to sse clients
      chatState.sseClients.forEach { client ->
        client.sendMessage(TextMessage(wsMessage))
      }
    }
  }
}
