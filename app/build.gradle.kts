@file:Suppress("UnstableApiUsage")

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("androidx.navigation.safeargs")
	id("com.google.dagger.hilt.android")
	id("com.google.devtools.ksp")
}
android {
	namespace = "com.souravroy.cleanproductapp"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.souravroy.cleanproductapp"
		minSdk = 24
		targetSdk = 34
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
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.14"
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
	val navVersion = "2.7.7"
	val hiltVersion = "2.51.1"
	val lifecycleVersion = "2.8.2"
	val roomVersion = "2.6.1"
	val coroutinesVersion = "1.7.3"

	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
	implementation("androidx.activity:activity-compose:1.9.0")
	implementation(platform("androidx.compose:compose-bom:2024.06.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.foundation:foundation-android:1.6.8")

	//view binding
	implementation("androidx.compose.ui:ui-viewbinding:1.6.8")

	//lifecycle
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

	//coil
	implementation("io.coil-kt:coil-compose:2.4.0")

	//jetpack navigation
	implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
	implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
	implementation("androidx.navigation:navigation-compose:$navVersion")

	//hilt
	implementation("com.google.dagger:hilt-android:$hiltVersion")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
	ksp("com.google.dagger:hilt-compiler:$hiltVersion")

	//retrofit
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.2.0")

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
	testImplementation("io.mockk:mockk:1.13.5")
	testImplementation("app.cash.turbine:turbine:1.0.0")
	androidTestImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	androidTestImplementation("io.mockk:mockk-android:1.13.5")
	androidTestImplementation("android.arch.core:core-testing:1.1.1")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}