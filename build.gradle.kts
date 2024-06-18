// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
	repositories {
		google()
	}
	dependencies {
		val navVersion = "2.7.7"
		classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
	}
}
plugins {
	id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
	id("com.android.application") version "8.5.0" apply false
	id("org.jetbrains.kotlin.android") version "1.8.10" apply false
	id("com.android.library") version "8.5.0" apply false
	id("com.google.dagger.hilt.android") version "2.44" apply false
}