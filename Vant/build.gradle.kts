plugins {
    // AGP 9.1.0 is the stable March 2026 release
    id("com.android.application") version "9.1.0" apply false
    // Compose compiler plugin — required since Kotlin 2.0; version must match Kotlin
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.20" apply false
    // KSP 2.3.6 — uses the new standalone versioning (no longer pairs with Kotlin)
    id("com.google.devtools.ksp") version "2.3.6" apply false
}
