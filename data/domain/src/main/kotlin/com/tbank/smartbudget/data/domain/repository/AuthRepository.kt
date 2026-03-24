package com.tbank.smartbudget.data.domain.repository

import com.tbank.smartbudget.data.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, name: String): Result<User>
    suspend fun getProfile(): Result<User>
    suspend fun logout()

    /**
     * Проверяет наличие пользователя в системе.
     * Возвращает true, если пользователь зарегистрирован, false - если нет.
     */
    suspend fun checkUserExistence(email: String): Result<Boolean>
}