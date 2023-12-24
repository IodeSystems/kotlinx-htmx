plugins {
  id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

nexusPublishing {
  repositories {
    sonatype()
  }
}
