plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "com.ai.gamelauncher"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.ai.gamelauncher"
		minSdk = 29
		targetSdk = 34
		versionCode = 1
		versionName = "1.0.0"

		vectorDrawables.useSupportLibrary = true
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
		debug {
			isMinifyEnabled = false
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlin {
		jvmToolchain(17)
	}

	buildFeatures {
		viewBinding = true
	}
}

dependencies {
	implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.0.0"))
	implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")

	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.appcompat:appcompat:1.7.0")
	implementation("com.google.android.material:material:1.12.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
	implementation("androidx.activity:activity-ktx:1.9.2")
	implementation("androidx.recyclerview:recyclerview:1.3.2")
	implementation("androidx.preference:preference-ktx:1.2.1")

	implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
	implementation("org.tensorflow:tensorflow-lite:2.14.0")
	implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
}