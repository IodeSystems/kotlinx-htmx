plugins {
  id("publish-conventions")
}

dependencies {
  implementation(libs.kotlinx.html)
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
  }
}


