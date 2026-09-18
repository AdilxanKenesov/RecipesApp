plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)

}

android {
    namespace = "uz.gita.recipesapp"
    compileSdk {
        version = release(37)
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    defaultConfig {
        applicationId = "uz.gita.recipesapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material.icons.extended)

    // Navigator
    implementation(libs.voyager.navigator)

    // Screen Model
    implementation(libs.voyager.screenmodel)

    // BottomSheetNavigator
    implementation(libs.voyager.bottom.sheet.navigator)

    // TabNavigator
    implementation(libs.voyager.tab.navigator)

    // Transitions
    implementation(libs.voyager.transitions)

    // Koin integration
    implementation(libs.voyager.koin)

    // Android

    // Hilt integration
    implementation(libs.voyager.hilt)

    // LiveData integration
    implementation(libs.voyager.livedata)

    // Core of Orbit, providing state management and unidirectional data flow (multiplatform)
    implementation(libs.orbit.core)
// Integrates Orbit with Android and Common ViewModel for lifecycle-aware state handling (Android, iOS, desktop)
    implementation(libs.orbit.viewmodel)
// Enables Orbit support for Jetpack Compose and Compose Multiplatform (Android, iOS, desktop)
    implementation(libs.orbit.compose)
// Simplifies testing with utilities for verifying state and event flows (multiplatform)
    testImplementation(libs.orbit.test)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.haze)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Retrofit & OkHttp
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)

    // Chucker Interceptor
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)


}