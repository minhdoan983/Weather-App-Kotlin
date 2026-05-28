// app/build.gradle.kts

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)           // Hilt
    kotlin("kapt")                              // Annotation processing cho Hilt/Room
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Đọc API key từ local.properties (không commit lên git)
        // Thêm vào local.properties: WEATHER_API_KEY=your_key_here
        buildConfigField("String", "WEATHER_API_KEY",
            "\"${project.findProperty("WEATHER_API_KEY") ?: ""}\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // ===== Jetpack Compose =====
    implementation(platform(libs.compose.bom))   // Bill of Materials — đồng bộ version tự động
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    // ===== Activity + ViewModel =====
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)  // collectAsStateWithLifecycle

    // ===== Navigation =====
    implementation(libs.navigation.compose)

    // ===== Hilt (Dependency Injection) =====
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)   // hiltViewModel() trong Composable

    // ===== Retrofit (Network) =====
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    // ===== DataStore (Local Storage) =====
    implementation(libs.datastore.preferences)

    // ===== Coroutines =====
    implementation(libs.coroutines.android)

    // ===== Room (nếu cần cache phức tạp) =====
    // implementation(libs.room.runtime)
    // implementation(libs.room.ktx)
    // kapt(libs.room.compiler)

    // ===== Testing =====
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.compose.ui.test.junit4)
}