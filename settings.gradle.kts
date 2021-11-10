enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    pluginManagement {
        val kotlinVersion: String by settings
        val androidGradleVersion: String by settings

        repositories {
            gradlePluginPortal()
            google()
            mavenCentral()
        }

        plugins {
            kotlin("jvm") version kotlinVersion
            kotlin("android") version kotlinVersion
            kotlin("kapt") version kotlinVersion
            id("com.android.application") version androidGradleVersion
        }

        resolutionStrategy {
            eachPlugin {
                when {
                    requested.id.id in listOf("com.android.application", "com.android.library") ->
                        useModule("com.android.tools.build:gradle:${target.version}")
                    requested.id.id.startsWith("dagger.hilt") ->
                        useModule("com.google.dagger:hilt-android-gradle-plugin:${target.version}")
                }
            }
        }
    }

//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) hmm??
}

rootProject.name = "PopMovie"

include(":app")
include(":core")
