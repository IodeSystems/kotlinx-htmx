plugins {
  // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
  id("common-conventions")

  // Apply the java-library plugin for API and implementation separation.
  application
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

dependencies {
  implementation(project(":htmx"))
  implementation(project(":spring"))
  implementation(libs.kotlinx.html)
  implementation(libs.spring.web)
  implementation(libs.spring.boot.starter.web)
  implementation(libs.spring.boot.starter.websocket)
  implementation(libs.spring.boot.starter.json)
  implementation(libs.jackson.module.kotlin)
}

testing {
  suites {
    // Configure the built-in test suite
    val test by getting(JvmTestSuite::class) {
      // Use Kotlin Test test framework
      useKotlinTest("1.9.20")
    }
  }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
  }
}

application {
  mainClass.set("com.iodesystems.htmx.example.ExampleKt")
}
