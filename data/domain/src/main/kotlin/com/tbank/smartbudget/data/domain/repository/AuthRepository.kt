package com.tbank.smartbudget.data.domain.repository

import com.tbank.smartbudget.core.network.remote.AppResult
import com.tbank.smartbudget.data.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<User>
    suspend fun register(email: String, password: String, name: String): AppResult<User>
    suspend fun getProfile(): AppResult<User>
    suspend fun updateProfile(name: String): AppResult<User>
    suspend fun logout()

    /**
     * Проверяет наличие пользователя в системе.
     * Возвращает true, если пользователь зарегистрирован, false - если нет.
     */
    suspend fun checkUserExistence(email: String): AppResult<Boolean>
}