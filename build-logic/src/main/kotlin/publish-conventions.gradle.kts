plugins {
  id("common-conventions")
  `java-library`
  `maven-publish`
  signing
}

java {
  withSourcesJar()
  withJavadocJar()
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      pom {
        name.set("kotlinx-htmx")
        description.set("Kotlinx HTMX extensions")
        url.set("https://github.com/IodeSystems/kotlinx-htmx")
        licenses {
          license { name.set("MIT License") }
        }
        developers {
          developer {
            id.set("nthalk")
            name.set("Carl Taylor")
            email.set("carl@etaylor.me")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/IodeSystems/kotlinx-htmx.git")
          developerConnection.set("scm:git:ssh://github.com/IodeSystems/kotlinx-htmx.git")
          url.set("https://github.com/IodeSystems/kotlinx-htmx")
        }
      }
    }
  }
}

signing {
  setRequired { project.version.toString().endsWith("-SNAPSHOT") }
  useGpgCmd()

  sign(publishing.publications["mavenJava"])
}
