@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinCompose)
    alias(libs.plugins.navigationSafeargs)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("de.mannodermaus.android-junit5")
    alias(libs.plugins.googleGmsGoogleServices)
}
android {
	namespace = "com.souravroy.cleanproductapp"
    compileSdk = 36

	defaultConfig {
		applicationId = "com.souravroy.cleanproductapp"
        minSdk = 26
        targetSdk = 36
		versionCode = 1
		versionName = "1.0"

        testInstrumentationRunner =
            "de.mannodermaus.junit5.provider.instrumentation.AndroidJUnitTestRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		debug {
			buildConfigField("String", "BASE_URL", "\"https://dummyjson.com/\"")
		}
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
			buildConfigField("String", "BASE_URL", "\"https://dummyjson.com/\"")
		}
	}
	compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
	}
    kotlinOptions {
        jvmTarget = "17"
    }
	buildFeatures {
		compose = true
		viewBinding = true
		buildConfig = true
	}
	packaging {
		resources {
			excludes += "/META-INF/*"
		}
	}
	testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
		packaging {
			jniLibs {
				useLegacyPackaging = true
			}
		}
	}
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.foundation.android)

	//view binding
    implementation(libs.androidx.compose.ui.viewbinding)

	//lifecycle
    implementation(libs.bundles.lifecycle)

	//coil
    implementation(libs.coil.compose)

	//jetpack navigation
    implementation(libs.bundles.navigation)

	//hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.compose.runtime)
    ksp(libs.hilt.compiler)

	//retrofit
    implementation(libs.bundles.retrofit)

	// Coroutines
    implementation(libs.bundles.coroutines)
    testImplementation(libs.test.coroutines)

	//room db
    implementation(libs.bundles.room)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

	//mockK
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.turbine)
    androidTestImplementation(libs.androidTest.mockk.android)
    androidTestImplementation(libs.androidTest.arch.core.testing)
    androidTestImplementation(libs.test.turbine)

    androidTestImplementation(libs.androidTest.jupiter.api)
    androidTestImplementation(libs.androidTest.jupiter.params)
    androidTestImplementation(libs.androidTest.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.debug.compose.ui.tooling)
    debugImplementation(libs.debug.compose.ui.test.manifest)
    androidTestRuntimeOnly(libs.androidTest.jupiter.engine)
    androidTestRuntimeOnly(libs.androidTest.junit.platform.runner)

    coreLibraryDesugaring(libs.desugarJdkLibs)
}