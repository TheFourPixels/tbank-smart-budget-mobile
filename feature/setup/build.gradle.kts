plugins {
    id("smartbudget.android.feature")
}

android {
    namespace = "com.tbank.smartbudget.feature.setup"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.data.domain)
    implementation(projects.feature.dashboard)
    implementation(projects.feature.operations)
}