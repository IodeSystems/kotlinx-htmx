import de.fayard.refreshVersions.core.StabilityLevel


pluginManagement {
  includeBuild("build-logic")

}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
  id("de.fayard.refreshVersions") version "0.60.5"
}

refreshVersions {
  rejectVersionIf {
    if (!candidate.stabilityLevel.isAtLeastAsStableAs(StabilityLevel.Stable)) return@rejectVersionIf true
    if (candidate.value.contains("rc", ignoreCase = true)) return@rejectVersionIf true
    false
  }
}


rootProject.name = "kotlinx-htmx"
include("htmx", "spring", "example")
