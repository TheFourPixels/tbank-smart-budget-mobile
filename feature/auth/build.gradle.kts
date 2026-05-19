plugins {
    id("smartbudget.android.feature")
}

android {
    namespace = "com.tbank.smartbudget.feature.auth"

    kotlin {
        target {
            compilerOptions {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
                freeCompilerArgs.add("-Xexplicit-backing-fields")

            }
        }
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.data.domain)
    implementation(projects.core.network)
    
    implementation(libs.androidx.compose.material.icons.extended)
}