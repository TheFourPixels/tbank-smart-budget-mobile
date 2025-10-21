// Этот файл находится в корневой директории проекта
plugins {
    // 1. Используйте ТОЛЬКО alias для плагина Android Application (если он есть в libs.versions.toml)
    alias(libs.plugins.android.application) apply false

    // 2. Используйте alias для плагинов Kotlin
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // 3. Используйте id для Hilt, явно указывая версию
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    // 4. (Опционально) Если вы используете Android Library, добавьте и ее:
    // alias(libs.plugins.android.library) apply false
}
