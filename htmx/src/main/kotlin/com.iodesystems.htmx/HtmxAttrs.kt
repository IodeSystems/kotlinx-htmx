package com.iodesystems.htmx

import kotlinx.html.FlowContent
import org.intellij.lang.annotations.Language
import kotlin.time.Duration

data class HtmxAttrs(val tag: FlowContent) {
  /**
   * The hx-post attribute allows you to specify the URL that will be used for an AJAX request.
   * See: https://htmx.org/attributes/hx-post/
   */
  fun post(path: String) {
    tag.attributes["hx-post"] = path
  }

  /**
   * The hx-ext attribute enables an htmx extension for an element and all its children.
   *
   * See: https://htmx.org/attributes/hx-ext/
   */
  fun ext(extension: String) {
    tag.attributes["hx-ext"] = extension
  }

  /**
   * Enable the JSON encoding extension.
   */
  fun json() {
    tag.attributes["hx-ext"] = "json-enc"
  }

  /**
   * The hx-put attribute allows you to specify the URL that will be used for an AJAX request.
   * See: https://htmx.org/attributes/hx-put/
   */
  fun put(path: String) {
    tag.attributes["hx-put"] = path
  }

  /**
   * The hx-patch attribute allows you to specify the URL that will be used for an AJAX request.
   * See: https://htmx.org/attributes/hx-patch/
   */
  fun patch(path: String) {
    tag.attributes["hx-patch"] = path
  }

  /**
   *  The hx-get attribute allows you to specify the URL that will be used for an AJAX request.
   *  See: https://htmx.org/attributes/hx-get/
   */
  fun get(path: String) {
    tag.attributes["hx-get"] = path
  }

  /**
   * The hx-delete attribute allows you to specify the URL that will be used for an AJAX request.
   * See: https://htmx.org/attributes/hx-delete/
   */
  fun delete(path: String) {
    tag.attributes["hx-delete"] = path
  }

  /**
   * The hx-trigger attribute allows you to specify what triggers an AJAX request.
   * See: https://htmx.org/attributes/hx-trigger/
   */
  fun trigger(block: HtmxTrigger.() -> Unit) {
    block(HtmxTrigger(tag))
  }


  /**
   * The hx-vals attribute allows you to specify a JSON object that will be sent as the body of the request.
   *
   * See: https://htmx.org/attributes/hx-vals/
   */
  fun vals(obj: String) {
    tag.attributes["hx-vals"] = obj
  }

  /**
   * The hx-ws allows you to work with Web Sockets directly from HTML.
   *
   * See: https://htmx.org/attributes/hx-ws/
   */
  fun ws(path: String) {
    tag.attributes["hx-ws"] = "connect:$path"
  }

  /**
   * The hx-ws allows you to work with Web Sockets directly from HTML.
   *
   * See: https://htmx.org/attributes/hx-ws/
   */
  fun wsSend(event: String) {
    tag.attributes["hx-ws"] = "send:$event"
  }

  // Server Sent Events
  fun sse(path: String, swapEvent: String? = null) {
    tag.attributes["hx-sse"] = "connect:$path"
    if (swapEvent != null) {
      tag.attributes["hx-sse"] += " swap:$swapEvent"
    }
  }

  /**
   * When using sse, this will swap the element with the response from the server.
   *
   * See: https://htmx.org/attributes/hx-sse/
   */
  fun sseSwap(event: String) {
    tag.attributes["hx-sse"] = "swap:$event"
  }


  /**
   * The hx-request attribute allows you to specify additional request parameters.
   * See: https://htmx.org/attributes/hx-request/
   */
  fun request(
    timeout: Duration? = null,
    noHeaders: Boolean? = null,
    credentials: Boolean? = null,
  ) {
    var request = ""
    if (timeout != null) {
      request += "\"timeout\":${timeout.inWholeMilliseconds}"
    }
    if (noHeaders != null) {
      if (request.isNotEmpty()) {
        request += ","
      }
      request += "\"noHeaders\":$noHeaders"
    }
    if (credentials != null) {
      if (request.isNotEmpty()) {
        request += ","
      }
      request += "\"credentials\":$credentials"
    }
    tag.attributes["hx-request"] = request
  }

  enum class Scroll {
    TOP, BOTTOM
  }

  enum class Swap {
    outerHTML,
    innerHTML,
    afterbegin,
    beforebegin,
    afterend,
    beforeend,
    none,
    morphdom,
  }

  /**
   * Swap the element with the response from the server.
   *
   * See: https://htmx.org/attributes/hx-swap-oob/
   */
  fun swapOob(
    mode: Swap = Swap.outerHTML,
    transition: Boolean = false,
    swap: Duration? = null,
    settle: Duration? = null,
    ignorTitle: Boolean = false,
    scroll: Scroll? = null,
  ) {
    return swap(
      mode = mode,
      transition = transition,
      swap = swap,
      settle = settle,
      ignorTitle = ignorTitle,
      scroll = scroll,
      oob = true
    )
  }

  /**
   * Swap the element with the response from the server.
   *
   * See: https://htmx.org/attributes/hx-swap/
   */
  fun swap(
    mode: Swap = Swap.outerHTML,
    transition: Boolean = false,
    swap: Duration? = null,
    settle: Duration? = null,
    ignorTitle: Boolean = false,
    scroll: Scroll? = null,
    show: Scroll? = null,
    oob: Boolean = false
  ) {
    var setting = mode.name
    if (transition) {
      setting += " transition:true"
    }
    if (settle != null) {
      setting += " settle:${settle.inWholeMilliseconds}ms"
    }
    if (ignorTitle) {
      setting += " ignoreTitle:true"
    }
    if (scroll != null) {
      setting += " scroll:${scroll.name.lowercase()}"
    }
    if (show != null) {
      setting += " show:${show.name.lowercase()}"
    }
    if (swap != null) {
      setting += " swap:${swap.inWholeMilliseconds}ms"
    }
    tag.attributes["hx-swap${if (oob) "-oob" else ""}"] = setting
  }

  /**
   * Simple onclick handler that binds with htmx.trigger
   */
  fun click(selector: String, event: String = "") {
    tag.attributes["onclick"] = "htmx.trigger('$selector', '$event')"
  }

  enum class FormEncoding(val value: String) {
    URL_ENCODED("application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data"),
  }

  /**
   * changes the request encoding type
   * See: https://htmx.org/attributes/hx-encoding/
   */
  fun encoding(encoding: FormEncoding) {
    tag.attributes["hx-encoding"] = encoding.value
  }

  /**
   * The hx-indicator attribute allows you to specify the element that will have the htmx-request class added to it for the duration of the request. This can be used to show spinners or progress indicators while the request is in flight.
   * See: https://htmx.org/attributes/hx-indicator/
   */
  fun indicator(selector: String) {
    tag.attributes["hx-indicator"] = selector
  }

  /**
   * The hx-target attribute allows you to target a different element for swapping than the one issuing the AJAX request.
   *
   * See: https://htmx.org/attributes/hx-target/
   */
  fun target(selector: String) {
    tag.attributes["hx-target"] = selector
  }

  /**
   * Add or remove progressive enhancement for links and forms
   * See: https://htmx.org/attributes/hx-boost/
   */
  fun boost(enabled: Boolean = true) {
    tag.attributes["hx-boost"] = enabled.toString()
  }

  /**
   * Pushes the URL into the browser location bar, creating a new history entry
   * See: https://htmx.org/attributes/hx-push-url/
   */
  fun pushUrl(enabled: Boolean) {
    tag.attributes["hx-push-url"] = enabled.toString()
  }

  /**
   * Pushes the URL into the browser location bar, creating a new history entry
   * See: https://htmx.org/attributes/hx-push-url/
   */
  fun pushUrl(url: String) {
    tag.attributes["hx-push-url"] = url
  }

  /**
   * The hx-replace-url attribute allows you to replace the current url of the browser location history.
   * See: https://htmx.org/attributes/hx-replace-url/
   */
  fun replaceUrl(enabled: Boolean) {
    tag.attributes["hx-replace-url"] = enabled.toString()
  }

  /**
   * The hx-replace-url attribute allows you to replace the current url of the browser location history.
   * See: https://htmx.org/attributes/hx-replace-url/
   */
  fun replaceUrl(url: String) {
    tag.attributes["hx-replace-url"] = url
  }

  /**
   * Set the hx-history attribute to false on any element in the current document, or any html fragment loaded into the current document by htmx, to prevent sensitive data being saved to the localStorage cache when htmx takes a snapshot of the page state.
   *
   * See: https://htmx.org/attributes/hx-history/
   */
  fun history(enabled: Boolean) {
    tag.attributes["hx-history"] = enabled.toString()
  }


  /**
   * the element to snapshot and restore during history navigation
   *
   * See: https://htmx.org/attributes/hx-history-elt/
   */
  fun historyElt() {
    tag.attributes["hx-history-elt"] = "true"
  }

  /**
   * handle any event with a script inline
   *
   * See: https://htmx.org/attributes/hx-on/
   */
  fun on(
    event: String,
    @Language("JavaScript")
    code: String
  ) {
    tag.attributes["hx-on:$event"] = code
  }

  /**
   * The hx-params attribute allows you to filter the parameters that will be submitted with an AJAX request.
   *
   * See: https://htmx.org/attributes/hx-params/
   */
  fun params(vararg params: String) {
    tag.attributes["hx-params"] = params.joinToString(",")
  }

  /**
   * Select the element to swap with the response from the server.
   * See: https://htmx.org/attributes/hx-select/
   */
  fun select(selector: String) {
    tag.attributes["hx-select"] = selector
  }

  /**
   * Select the element to swap with the response from the server.
   * See: https://htmx.org/attributes/hx-select-oob/
   */
  fun selectOob(selector: String) {
    tag.attributes["hx-select-oob"] = selector
  }

  /**
   * The hx-disable attribute will disable htmx processing for a given element and all its children. This can be useful as a backup for HTML escaping, when you include user generated content in your site, and you want to prevent malicious scripting attacks.
   *
   * The value of the tag is ignored, and it cannot be reversed by any content beneath it.
   * See: https://htmx.org/attributes/hx-boosted/
   */
  fun disable() {
    tag.attributes["hx-disabled"] = "true"
  }

  /**
   * The hx-disabled-elt attribute allows you to specify elements that will have the disabled attribute added to them for the duration of the request.
   * See: https://htmx.org/attributes/hx-disabled-elt/
   */
  fun disableElt() {
    tag.attributes["hx-disabled-elt"] = "true"
  }

  /**
   * The default behavior for htmx is to “inherit” many attributes automatically: that is, an attribute such as hx-target may be placed on a parent element, and all child elements will inherit that target.
   *
   * See: https://htmx.org/attributes/hx-disinherit/
   */
  fun disinherit(vararg selector: String = arrayOf("*")) {
    tag.attributes["hx-disinherit"] = selector.joinToString(" ")
  }

  /**
   * The hx-headers attribute allows you to add to the headers that will be submitted with an AJAX request.
   *
   * See: https://htmx.org/attributes/hx-headers/
   */
  fun headers(map: Map<String, String>) {
    tag.attributes["hx-headers"] = map.entries.joinToString(",") { (key, value) ->
      val escapedValue = value.replace("\"", "\\\"")
      "\"$key\":\"$escapedValue\""
    }
  }

  /**
   * The hx-include attribute allows you to include additional element values in an AJAX request.
   * See: https://htmx.org/attributes/hx-include/
   */
  fun include(selector: String) {
    tag.attributes["hx-include"] = selector
  }

  /**
   * The hx-preserve attribute allows you to keep an element unchanged during HTML replacement. Elements with hx-preserve set are preserved by id when htmx updates any ancestor element. You must set an unchanging id on elements for hx-preserve to work. The response requires an element with the same id, but its type and other attributes are ignored.
   * See: https://htmx.org/attributes/hx-preserve/
   */
  fun preserve() {
    tag.attributes["hx-preserve"] = "true"
  }

}
