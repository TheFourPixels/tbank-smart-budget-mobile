plugins {
    id("smartbudget.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.smartbudget.core.navigation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}