plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "io.github.xfy9326.glance"
    compileSdk = 32

    defaultConfig {
        applicationId = "io.github.xfy9326.glance"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            packagingOptions {
                resources.excludes += "DebugProbesKt.bin"
            }

            splits {
                abi {
                    isEnable = true
                    reset()
                    include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
                    isUniversalApk = true
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-rc02"
    }

    packagingOptions {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        resources.excludes += "/META-INF/CHANGES"
        resources.excludes += "/META-INF/README.md"
        resources.excludes += "/okhttp3/internal/publicsuffix/NOTICE"
    }
}

dependencies {
    // Project
    implementation(project(":app:ml"))

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")

    // ATools
    val aToolsVersion = "0.0.17"
    implementation("io.github.xfy9326.atools:atools-core:$aToolsVersion")
    implementation("io.github.xfy9326.atools:atools-io:$aToolsVersion")
    implementation("io.github.xfy9326.atools:atools-compose:$aToolsVersion")
    implementation("io.github.xfy9326.atools:atools-coroutines:$aToolsVersion")

    // Coli
    implementation("io.coil-kt:coil-compose:2.1.0")

    // AndroidX
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.core:core-splashscreen:1.0.0-rc01")

    // AndroidX Customview
    implementation("androidx.customview:customview:1.1.0")

    // AndroidX Activity
    val activityVersion = "1.5.0"
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.activity:activity-compose:$activityVersion")

    // AndroidX Lifecycle
    val lifeCycleVersion = "2.5.0"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifeCycleVersion")

    // AndroidX CameraX
    val cameraVersion = "1.1.0-rc02"
    implementation("androidx.camera:camera-camera2:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraVersion")

    // Jetpack Compose
    val composeVersion = "1.2.0-rc02"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-core:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")

    val accompanistVersion = "0.24.12-rc"
    implementation("com.google.accompanist:accompanist-insets-ui:$accompanistVersion")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")

    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}