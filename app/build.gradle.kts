import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":core"))

    // Android Compose
    implementation(libs.bundles.androidKtx)
    implementation(libs.bundles.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.uiTooling)

    // Dagger Hilt
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    androidTestImplementation(libs.hiltAndroidTesting)
    kaptAndroidTest(libs.hiltCompiler)

    testImplementation(libs.hiltAndroidTesting)
    kaptTest(libs.hiltCompiler)

    // AirBNB Mavericks
    implementation(libs.bundles.mavericks)

    // Network
    implementation(libs.flowreactivenetwork)
    implementation(libs.coilCompose)
    implementation(libs.bundles.retrofit)
    implementation(libs.okhttp)
}

android {
    compileSdk = 31

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }

    defaultConfig {
        applicationId = "com.example.popmovie"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val themoviedbApikey: String = gradleLocalProperties(rootDir).getProperty("themoviedbApikey")
        debug {
            buildConfigField("String", "THEMOVIEDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "THEMOVIEDB_IMAGE_URL", "\"https://image.tmdb.org/t/p/\"")
            buildConfigField("String", "THEMOVIEDB_API_KEY", themoviedbApikey)
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "THEMOVIEDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "THEMOVIEDB_IMAGE_URL", "\"https://image.tmdb.org/t/p/\"")
            buildConfigField("String", "THEMOVIEDB_API_KEY", themoviedbApikey)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}
