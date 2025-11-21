enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Messenger"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":vaultchat-server")
include(":vaultchat-android")
