@file:Suppress("UnstableApiUsage")

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
	id("androidx.navigation.safeargs")
	id("com.google.dagger.hilt.android")
	id("com.google.devtools.ksp")
}
android {
	namespace = "com.souravroy.cleanproductapp"
    compileSdk = 36

	defaultConfig {
		applicationId = "com.souravroy.cleanproductapp"
		minSdk = 24
        targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
	}
    kotlin {
        jvmToolchain(8)
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
		packaging {
			jniLibs {
				useLegacyPackaging = true
			}
		}
	}
}

dependencies {
    val navVersion = "2.9.6"
    val hiltVersion = "2.57.2"
    val lifecycleVersion = "2.10.0"
    val roomVersion = "2.8.4"
    val coroutinesVersion = "1.10.2"
    val retrofitVersion = "3.0.0"

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.activity:activity-compose:1.12.0")
    implementation(platform("androidx.compose:compose-bom:2025.11.01"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation-android:1.9.5")

	//view binding
    implementation("androidx.compose.ui:ui-viewbinding:1.9.5")

	//lifecycle
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

	//coil
    implementation("io.coil-kt:coil-compose:2.7.0")

	//jetpack navigation
	implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
	implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
	implementation("androidx.navigation:navigation-compose:$navVersion")

	//hilt
	implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
	ksp("com.google.dagger:hilt-compiler:$hiltVersion")

	//retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

	// Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

	//room db
	implementation("androidx.room:room-runtime:$roomVersion")
	annotationProcessor("androidx.room:room-compiler:$roomVersion")

	// To use Kotlin annotation processing tool (kapt)
	ksp("androidx.room:room-compiler:$roomVersion")

	// optional - Kotlin Extensions and Coroutines support for Room
	implementation("androidx.room:room-ktx:$roomVersion")

	//mockK
    testImplementation("io.mockk:mockk:1.14.6")
    testImplementation("app.cash.turbine:turbine:1.2.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    androidTestImplementation("io.mockk:mockk-android:1.14.6")
	androidTestImplementation("android.arch.core:core-testing:1.1.1")

	testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.11.01"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}