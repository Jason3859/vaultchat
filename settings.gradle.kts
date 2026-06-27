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
include(":main")
include(":feature")
include(":feature:logout")
include(":feature:device")
include(":feature:connections")
include(":feature:user")
include(":feature:messaging")
include(":feature:messages")
include(":feature:blocklist")
include(":ui")
include(":ui:auth")
include(":ui:auth:abstract")
include(":ui:auth:concrete")
include(":ui:main")
include(":ui:main:abstract")
include(":ui:main:concrete")
include(":feature:open-links")
include(":share")
