plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.jason.app.compose.vaultchat.feature.open_links"
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
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
}