enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SmartBudget"
include(":app")
include(":core:ui")
include(":core:network")
include(":core:navigation")
include(":core:datastore")
include(":data:domain")
include(":data:repository")
include(":feature:auth")
include(":feature:category-search")
include(":feature:dashboard")
include(":feature:operations")
include(":feature:home")
include(":feature:budget-details")
include(":feature:profile")
include(":feature:setup")
include(":feature:selected-categories")
include(":feature:budget-edit")
