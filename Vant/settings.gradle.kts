pluginManagement {
    repositories {
        google()            // Essential for Android Plugins
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()            // Essential for Compose/Material3 libraries
        mavenCentral()
    }
}

rootProject.name = "Vant"
include(":app")
