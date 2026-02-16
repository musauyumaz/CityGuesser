import org.gradle.kotlin.dsl.support.kotlinCompilerOptions

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.musauyumaz.cityguesser"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.musauyumaz.cityguesser"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }
    composeOptions{
        kotlinCompilerExtensionVersion = "1.15.15"
    }
    packaging{
        resources{
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM (Bill of Materials - tüm Compose versiyonlarını yönetir)
    implementation(platform(libs.androidx.compose.bom.v20260200))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation - Ekranlar arası geçiş için
    implementation(libs.androidx.navigation.compose)

    // ViewModel - State yönetimi için
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Coil - Resim yükleme kütüphanesi (Unsplash fotoğrafları için)
    implementation(libs.coil.compose)

    // Retrofit - API çağrıları için
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // OkHttp - HTTP client (Retrofit ile birlikte çalışır)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Coroutines - Asenkron işlemler için
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Gson - JSON parsing için
    implementation(libs.gson)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20260200))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}