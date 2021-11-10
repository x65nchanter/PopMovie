plugins {
    kotlin("android") apply false
    kotlin("jvm") apply false
    kotlin("kapt") apply false
    id("com.android.application") apply false
    id("dagger.hilt.android.plugin") version "2.39.1" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
