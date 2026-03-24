plugins {
    id("smartbudget.android.library")
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.smartbudget.data.repository"
}

dependencies {
    implementation(projects.data.domain)
    implementation(projects.core.network)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(projects.core.datastore)
    implementation(libs.retrofit.core)

}