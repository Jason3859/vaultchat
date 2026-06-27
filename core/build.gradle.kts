plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "dev.jason.app.compose.vaultchat.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)

    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.compose.adaptive.layout)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}