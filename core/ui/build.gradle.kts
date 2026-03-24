plugins {
    id("smartbudget.android.library")
    id("smartbudget.android.compose")
}

android {
    namespace = "com.tbank.smartbudget.core.ui"
}

dependencies {
    implementation(projects.core.network)
    implementation(libs.androidx.compose.material.icons.extended)
}