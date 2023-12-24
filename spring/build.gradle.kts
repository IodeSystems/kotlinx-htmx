plugins {
  id("publish-conventions")
}

dependencies {
  implementation(project(":htmx"))
  implementation(libs.kotlinx.html)
  implementation(libs.spring.web)
}
