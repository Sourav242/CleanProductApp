plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("androidx.navigation.safeargs")
	id("kotlin-kapt")
	id("com.google.dagger.hilt.android")
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
		kotlinCompilerExtensionVersion = "1.4.5"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	val navVersion = "2.6.0"
	val hiltVersion = "2.45"
	val lifecycleVersion = "2.6.2"
	val roomVersion = "2.5.2"

	implementation("androidx.core:core-ktx:1.9.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
	implementation("androidx.activity:activity-compose:1.7.2")
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.foundation:foundation-android:1.5.1")

	//view binding
	implementation("androidx.compose.ui:ui-viewbinding:1.4.3")

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
	implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
	kapt("com.google.dagger:hilt-compiler:$hiltVersion")

	//retrofit
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.2.0")

	// Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

	//room db
	implementation("androidx.room:room-runtime:$roomVersion")
	annotationProcessor("androidx.room:room-compiler:$roomVersion")

	// To use Kotlin annotation processing tool (kapt)
	kapt("androidx.room:room-compiler:$roomVersion")

	// optional - Kotlin Extensions and Coroutines support for Room
	implementation("androidx.room:room-ktx:$roomVersion")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}