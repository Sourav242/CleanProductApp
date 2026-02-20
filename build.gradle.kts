plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.navigationSafeargs) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    id("de.mannodermaus.android-junit5") version "2.0.1" apply false
    alias(libs.plugins.googleGmsGoogleServices) apply false
}
