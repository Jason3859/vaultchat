plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.jason.app.compose.vaultchat.ui"
    compileSdk {
        version = release(37)
    }
}