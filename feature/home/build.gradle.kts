plugins {
    id("smartbudget.android.feature")
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tbank.smartbudget.feature.home"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.data.domain)
    implementation(projects.feature.dashboard)
    implementation(projects.feature.operations)
    implementation(projects.core.datastore)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}