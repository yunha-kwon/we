plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.gradle.plugin)
    id("kotlin-kapt")
//    id("com.android.application")

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.we.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    //module
    implementation(project(":data"))
    implementation(project(":presentation"))

    //androidx & test
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)


    // Log
    implementation(libs.timber)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.kotlinxSerialization)
    implementation(libs.okhttp)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.sclars)

    // DataStore
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)

    // firebase

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)


}