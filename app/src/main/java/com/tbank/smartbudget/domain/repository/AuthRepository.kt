package com.tbank.smartbudget.domain.repository

interface AuthRepository {
    /**
     * Проверяет существование пользователя.
     * @return Имя пользователя, если он существует, или null, если это новый пользователь.
     */
    suspend fun checkUserExistence(email: String): Result<String?>
}