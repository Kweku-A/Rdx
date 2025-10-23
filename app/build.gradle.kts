plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21"
    id("kotlin-parcelize")
    kotlin("kapt")
    alias(libs.plugins.hiltPlugin)
    kotlin("plugin.serialization") version "1.9.22"
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.10" // or latest available
}


android {
    namespace = "com.kweku.armah.rdx"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kweku.armah.rdx"
        minSdk = 24
        targetSdk = 36
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

        getByName("debug") {
            enableUnitTestCoverage = false // Enable coverage when running tests
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // Add this block to enable Robolectric
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

// In app/build.gradle.kts

// In app/build.gradle.kts

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val mainSrc = "$projectDir/src/main/java"
    sourceDirectories.setFrom(files(mainSrc))

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "android/**/*.*",
        "**/*Test*.*",
        "**/di/*",
        "**/models/*"
    )

    classDirectories.setFrom(
        fileTree("$buildDir/tmp/kotlin-classes/debug") {
            exclude(fileFilter)
        }
    )

    executionData.setFrom(
        fileTree(buildDir) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "jacoco/testDebugUnitTest.exec",
                "jacoco/testDebugUnitTest.exec.gz"
            )
        }
    )
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
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.splashscreen)
    kapt(libs.hilt.compiler)
}
dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.turbine)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // For MockK (mocking library)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.datastore.preferences)
    testImplementation(libs.robolectric)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // Robolectric for running Android framework code on the JVM
    testImplementation(libs.robolectric)

    // Compose UI Testing dependencies
    // These work with both local (Robolectric) and instrumented tests
    testImplementation(libs.androidx.compose.ui.test.junit4)

    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.junit.ktx)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
