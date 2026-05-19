package com.tbank.smartbudget.core.datastore

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

/**
 * Класс для управления сессией пользователя (сохранение токена и ID).
 */
@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "smart_budget_session"
        const val KEY_AUTH_TOKEN = "auth_token"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_NAME = "user_name"
    }

    /**
     * Сохраняет данные сессии после успешного входа/регистрации.
     */
    fun saveAuthData(token: String, userId: Long, name: String) {
        prefs.edit {
            putString(KEY_AUTH_TOKEN, token)
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, name)
        }
    }

    /**
     * Обновляет имя пользователя.
     */
    fun updateUserName(name: String) {
        prefs.edit {
            putString(KEY_USER_NAME, name)
        }
    }

    /**
     * Получает сохраненный токен.
     */
    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Получает ID текущего пользователя.
     */
    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1)
    }

    /**
     * Получает имя пользователя.
     */
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    /**
     * Очищает данные сессии (выход из системы).
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}