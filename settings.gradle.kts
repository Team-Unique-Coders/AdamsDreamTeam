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

rootProject.name = "AdamDreamTeam"
include(":app")
include(":data")
include(":bank")
include(":chat")
include(":handyman")
include(":mechanic")
include(":delivery")
include(":eat")
include(":hotel")
include(":tinder")
include(":laundry")
include(":learn")
include(":doctor")
include(":uber")
