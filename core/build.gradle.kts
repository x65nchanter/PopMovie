
plugins {
    kotlin("jvm")
    `java-library`
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }
}