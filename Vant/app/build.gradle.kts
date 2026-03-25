plugins {
    id("com.android.application")
    // Compose compiler plugin — replaces composeOptions.kotlinCompilerExtensionVersion in Kotlin 2.0+
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.vant.tracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vant.tracker"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    // ── Release signing ───────────────────────────────────────────────────────
    signingConfigs {
        create("release") {
            storeFile     = rootProject.file(System.getenv("KEYSTORE_PATH") ?: "keystore/vant-release.jks")
            storePassword = System.getenv("KEYSTORE_PASS") ?: "vant2026!"
            keyAlias      = System.getenv("KEY_ALIAS")    ?: "vant"
            keyPassword   = System.getenv("KEY_PASS")     ?: "vant2026!"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

// JVM toolchain — ensures the Kotlin compiler, javac, and tests all use JDK 17.
kotlin {
    jvmToolchain(17)
}

dependencies {
    // Compose Bill of Materials — keeps all Compose library versions in sync
    val composeBom = platform("androidx.compose:compose-bom:2026.01.00")
    implementation(composeBom)

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Lifecycle / ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")

    // Room — offline-first local database
    val roomVersion = "2.7.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Retrofit — TMDB API networking
    val retrofitVersion = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
