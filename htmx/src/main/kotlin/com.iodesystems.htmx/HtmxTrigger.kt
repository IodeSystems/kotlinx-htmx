package com.iodesystems.htmx

import kotlinx.html.FlowContent
import kotlin.time.Duration

data class HtmxTrigger(val tag: FlowContent) {

  fun event(event: String) {
    if (tag.attributes["hx-trigger"] == null) {
      tag.attributes["hx-trigger"] = event
    } else {
      tag.attributes["hx-trigger"] += ",$event"
    }
  }

  fun click() = event("click")

  fun mouseenter() = event("mouseenter")

  fun mouseleave() = event("mouseleave")

  /**
   * triggered when an element is scrolled into the viewport (also useful for lazy-loading). If you are using overflow in css like overflow-y: scroll you should use intersect once instead of revealed.
   */
  fun revealed() = event("revealed")

  /**
   * fires once when an element first intersects the viewport.
   */
  fun intersects(
    selector: String? = null,
    threshold: Float? = null,
  ) {
    var trigger = "intersects"
    if (selector != null) {
      trigger += "root:$selector"
    }
    if (threshold != null) {
      trigger += "threshold:$threshold"
    }
    event(trigger)
  }

  /**
   *  triggered on load (useful for lazy-loading something)
   */
  fun load() = event("load")

  fun sse(event: String) = event("sse:$event")

  fun every(time: Duration) = event("every ${time.inWholeMilliseconds}ms")

  fun keyup() = event("keyup")

  fun withKey(key: String) {
    tag.attributes["hx-trigger"] += "[$key]"
  }

  /**
   * the event will only trigger once (e.g. the first click)
   */
  fun once() {
    tag.attributes["hx-trigger"] += " once"
  }

  /**
   * the event will only change if the value of the element has changed. Please pay attention change is the name of the event and changed is the name of the modifier.
   */
  fun changed() {
    tag.attributes["hx-trigger"] += " changed"
  }

  /**
   * a delay will occur before an event triggers a request. If the event is seen again it will reset the delay.
   * See: https://htmx.org/attributes/hx-trigger/
   */
  fun delay(duration: Duration) {
    tag.attributes["hx-trigger"] += " delay:${duration.inWholeMilliseconds}ms"
  }

  /**
   *  a throttle will occur after an event triggers a request. If the event is seen again before the delay completes, it is ignored, the element will trigger at the end of the delay.
   *  See: https://htmx.org/attributes/hx-trigger/
   */
  fun throttle(duration: Duration) {
    tag.attributes["hx-trigger"] += " throttle:${duration.inWholeMilliseconds}ms"
  }


  /**
   * allows the event that triggers a request to come from another element in the document (e.g. listening to a key event on the body, to support hot keys)
   * See: https://htmx.org/attributes/hx-trigger/
   */
  fun from(selector: String) {
    tag.attributes["hx-trigger"] += " from:$selector"
  }

  /**
   * allows you to filter via a CSS selector on the target of the event. This can be useful when you want to listen for triggers from elements that might not be in the DOM at the point of initialization, by, for example, listening on the body, but with a target filter for a child element
   */
  fun target(selector: String) {
    tag.attributes["hx-trigger"] += " target:$selector"
  }

  enum class Queue {
    first,
    last,
    all,
    none
  }

  /**
   * etermines how events are queued if an event occurs while a request for another event is in flight
   * See: https://htmx.org/attributes/hx-trigger/
   */
  fun queue(mode: Queue) {
    tag.attributes["hx-trigger"] += " queue:${mode.name}"
  }

  /**
   *  if this option is included the event will not trigger any other htmx requests on parents (or on elements listening on parents)
   *  See: https://htmx.org/attributes/hx-trigger/
   */
  fun consume() {
    if (tag.attributes["hx-trigger"] == null) {
      tag.attributes["hx-trigger"] = "consume"
    } else {
      tag.attributes["hx-trigger"] += " consume"
    }
  }
}
