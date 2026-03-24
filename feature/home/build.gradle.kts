plugins {
    id("smartbudget.android.feature")
}

android {
    namespace = "com.tbank.smartbudget.feature.home"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.data.domain)
    implementation(projects.feature.dashboard)
    implementation(projects.feature.operations)
    implementation(projects.core.datastore)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material.icons.extended)
}