plugins {
    id("smartbudget.android.feature")
}

android {
    namespace = "com.tbank.smartbudget.feature.auth"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.data.domain)
}