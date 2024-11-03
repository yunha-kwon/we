plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.gradle.plugin)
    id("kotlin-kapt")
    alias(libs.plugins.navigation.safe.args)
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.we.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":model"))
    //androidX & test
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.biometric)


    //Test
    testImplementation(libs.junit)
    testImplementation(libs.coroutine.test)
    testImplementation(libs.google.truth)
    testImplementation(libs.mock)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.google.truth)

    // Lifecycle
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.extensions)

    // Permission
    implementation(libs.ted.permission.normal)
    implementation(libs.ted.permission.coroutine)

    // Log
    implementation(libs.timber)

    // Glide
    implementation(libs.glide)

    // Coroutine
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)

    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.fragment)

    // Dotindicator
    implementation(libs.dotsindicator)

    //Lottie
    implementation(libs.lottie)

    //FCM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation(libs.kakao.share)
}