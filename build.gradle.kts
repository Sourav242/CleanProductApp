// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
	repositories {
		google()
	}
	dependencies {
        val navVersion = "2.9.6"
		classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
	}
}
plugins {
    // The KSP version must match the Kotlin version for compatibility.
    // For Kotlin 2.2.21, you should use a corresponding KSP version.
    id("com.google.devtools.ksp") version "2.2.21-2.0.4" apply false
    id("com.android.application") version "8.13.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" apply false
    id("com.android.library") version "8.13.1" apply false
    id("com.google.dagger.hilt.android") version "2.57.2" apply false
}