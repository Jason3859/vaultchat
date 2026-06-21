plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.jason.app.compose.vaultchat.ui.main"
    compileSdk {
        version = release(37)
    }
}