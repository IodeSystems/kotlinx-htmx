# kotlinx-htmx

Ever wanted to... build (?) your no-build web application in kotlin?

`kotlinx-htmx` supports 100% of the apis from [htmx.org](https://htmx.org/) on top of `kotlinx.html`.

## TL;DR
```kotlin
data class Counter(val count: Int)
@PostMapping("/counter")
fun counterButton(
  @RequestBody counter: Counter = Counter(0)
) = Htmx {
  if (counter.count > 0) {
    // Let the indicator show!
    Thread.sleep(1.seconds.toJavaDuration())
  }
  div{
    id = "counter"
    div{
      id = "indicator"
      +"Loading..."
    }
    button {
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
}
```
See the `example` project for more details.

## Installation
Maven:
```xml
<!-- file: pom.xml-->
<dependencies>
  ...
  <dependency>
    <groupId>com.iodesystems.kotlinx-htmx</groupId>
    <artifactId>htmx</artifactId>
    <version>${check-mvn-repository}</version>
  </dependency>
  <!-- If you want to use it with spring-web -->
  <dependency>
    <groupId>com.iodesystems.kotlinx-htmx</groupId>
    <artifactId>spring</artifactId>
    <version>${check-mvn-repository}</version>
  </dependency>
  ...
</dependencies>
```

Gradle:
```kotlin
// file: build.gradle.kts
repositories {
    mavenCentral()
}
dependencies {
    implementation("com.iodesystems.kotlinx-htmx:htmx:${check-mvn-repository}")
    // If you want to use it with spring-web
    implementation("com.iodesystems.kotlinx-htmx:spring:${check-mvn-repository}")
}
```

## Spring Integration
To be able to return `Htmx` from your controller, you need to add `HtmxHttpMessageConverter` to your `WebMvcConfigurer`:
```kotlin
@Configuration
open class WebConfig : WebMvcConfigurer {
  override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
    converters.add(0, HtmxHttpMessageConverter())
  }
}
```

# License

MIT License
