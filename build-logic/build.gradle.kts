plugins {
    `kotlin-dsl`
}

group = "com.smartbudget.buildlogic"

dependencies {
    implementation(libs.plugins.android.application.get().let { "${it.pluginId}:${it.pluginId}.gradle.plugin:${libs.versions.agp.get()}" })
    implementation(libs.plugins.kotlin.android.get().let { "${it.pluginId}:${it.pluginId}.gradle.plugin:${libs.versions.kotlin.get()}" })
    implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:${libs.versions.kotlin.get()}")
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "smartbudget.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "smartbudget.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "smartbudget.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}