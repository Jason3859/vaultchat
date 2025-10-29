import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

repositories {
    google()
    mavenCentral()
}

room {
    schemaDirectory("$projectDir/schema")
}

android {
    namespace = namespace()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {
    ksp(libs.androidx.room.compiler)
    implementation(libs.koin.android)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.runtime.android)
    implementation(projects.messengerAndroid.localStorage.roomDb.roomDbDomain)
}