plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.gradle.plugin)
    id("kotlin-kapt")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.data.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":model"))

    //androidx & test
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Log
    implementation(libs.timber)


    // Coroutine
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)

    // DI
    implementation(libs.hilt.android)
    androidTestImplementation(libs.hilt.android.testing)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)


    // Network
    implementation(libs.retrofit)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.retrofit.converter.kotlinxSerialization)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.gson)

    // DataStore
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)
}