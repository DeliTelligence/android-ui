plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.apollo)
    alias(libs.plugins.kotlinKapt)
}


apollo {
    service("service") {
        packageName.set("com.example.delitelligence")

    }
}

android {
    namespace = "com.example.delitelligencefrontend"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.delitelligencefrontend"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.hilt.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.foundation.android)
    implementation(libs.accessibility.test.framework)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.animation.core.lint)
    ksp(libs.hilt.compiler)

    implementation(libs.coil.compose)  // Add Coil Compose for image loading

    implementation (libs.mpandroidchart)

    implementation(libs.accompanist.permissions)

    implementation(libs.okhttp)
    implementation (libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)   // Gson converter for Retrofit (optional)

    implementation(libs.apollo.runtime)

    implementation(libs.hilt.navigation.compose) // Using version catalog for Hilt Navigation Compose
    implementation(libs.apollo.api)

    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.mapstruct)
    kapt(libs.mapstruct.processor) // Use kapt for Mapstruct processor
}