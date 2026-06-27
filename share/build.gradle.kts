plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "dev.jason.app.compose.vaultchat.share"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {

    implementation(projects.feature.connections)
    implementation(projects.feature.messaging)
    implementation(projects.feature.messages)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
}