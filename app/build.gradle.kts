import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.aboutlibraries)
    id("kotlin-parcelize")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

val admobAppId = localProperties.getProperty("ADMOB_APP_ID") ?: ""
val admobNativeAdId = localProperties.getProperty("ADMOB_NATIVE_AD_UNIT_ID") ?: ""

val releaseKeystoreFilePath = localProperties.getProperty("KEYSTORE_FILE") ?: ""
val releaseKeystorePassword = localProperties.getProperty("KEYSTORE_PASSWORD") ?: ""
val releaseKeyAlias = localProperties.getProperty("KEY_ALIAS") ?: ""
val releaseKeyPassword = localProperties.getProperty("KEY_PASSWORD") ?: ""

android {
    namespace = "com.toyprojects.card_pilot"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.toyprojects.card_pilot"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (releaseKeystoreFilePath.isNotBlank()) {
                val keystoreFile = rootProject.file(releaseKeystoreFilePath)
                if (keystoreFile.exists()) {
                    storeFile = keystoreFile
                    storePassword = releaseKeystorePassword
                    keyAlias = releaseKeyAlias
                    keyPassword = releaseKeyPassword
                }
            }
        }
    }

    buildTypes {
        debug {
            // test ID
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
            buildConfigField("String", "NATIVE_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/2247696110\"")
        }
        release {
            if (admobAppId.isBlank() || admobNativeAdId.isBlank()) {
                throw GradleException("Real AdMob ID is required for release build. Please set ADMOB_APP_ID and ADMOB_NATIVE_AD_UNIT_ID in local.properties")
            }
            if (releaseKeystoreFilePath.isBlank() || !rootProject.file(releaseKeystoreFilePath).exists()) {
                throw GradleException("Release keystore file is required. Please set valid KEYSTORE_FILE in local.properties")
            }
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            manifestPlaceholders["admobAppId"] = admobAppId
            buildConfigField("String", "NATIVE_AD_UNIT_ID", "\"$admobNativeAdId\"")

            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += setOf("META-INF/DEPENDENCIES")
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.datastore.preferences)
    implementation(libs.play.app.update)

    // google drive & auth
    implementation(libs.play.services.auth)
    implementation(libs.google.api.client.android)
    implementation(libs.google.api.services.drive)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.google.http.client.gson)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.reorderable)
    implementation(libs.coil.compose)
    implementation(libs.androidx.palette.ktx)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // admob
    implementation(libs.play.services.ads)

    // aboutlibraries
    implementation(libs.aboutlibraries.compose)
}

val copyAboutLibraries = tasks.register<Copy>("copyAboutLibraries") {
    dependsOn("exportLibraryDefinitions")
    from(layout.buildDirectory.dir("generated/aboutLibraries"))
    include("aboutlibraries.json")
    into(layout.buildDirectory.dir("generated/aboutLibrariesRes/raw"))
}

android {
    sourceSets {
        getByName("main") {
            res.srcDir(layout.buildDirectory.dir("generated/aboutLibrariesRes").get().asFile)
        }
    }
}

tasks.named("preBuild") {
    dependsOn(copyAboutLibraries)
}

