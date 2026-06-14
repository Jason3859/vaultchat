enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "VaultChat"

include(":app")
include(":core")
include(":auth")
include(":messaging")
include(":messaging:domain")
include(":messaging:data")
include(":messaging:ui")
include(":feature-service")
include(":feature-service:logout")
include(":feature-service:device")
include(":feature-service:connections")
include(":feature-service:user")
include(":feature-service:messaging")
include(":feature-service:messages")
include(":ui:abstract")
include(":ui:abstract:auth")
include(":ui:abstract:messaging")
include(":ui:concrete:auth")
