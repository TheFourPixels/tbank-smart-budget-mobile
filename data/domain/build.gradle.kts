plugins {
    id("smartbudget.android.library")
}

android {
    namespace = "com.example.smartbudget.data.domain"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)


    implementation("javax.inject:javax.inject:1")
    implementation(projects.core.network)
    implementation(libs.androidx.compose.runtime.annotation)

}