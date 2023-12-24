plugins {
  id("io.github.gradle-nexus.publish-plugin")

}

nexusPublishing {
  repositories {
    sonatype()
  }
}

