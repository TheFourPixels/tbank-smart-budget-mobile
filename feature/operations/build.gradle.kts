plugins {
    id("smartbudget.android.feature")
}

android {
    namespace = "com.tbank.smartbudget.feature.operations"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.data.domain)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material.icons.extended)
}