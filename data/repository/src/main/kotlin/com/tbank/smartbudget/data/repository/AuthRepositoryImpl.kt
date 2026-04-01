package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.datastore.SessionManager
import com.tbank.smartbudget.core.network.remote.AppResult
import com.tbank.smartbudget.core.network.remote.api.AuthApi
import com.tbank.smartbudget.core.network.remote.api.UserApi
import com.tbank.smartbudget.core.network.remote.dto.CheckEmailRequest
import com.tbank.smartbudget.core.network.remote.dto.LoginRequest
import com.tbank.smartbudget.core.network.remote.dto.RegisterRequest
import com.tbank.smartbudget.core.network.remote.safeApiCall
import com.tbank.smartbudget.data.domain.model.User
import com.tbank.smartbudget.data.domain.model.UserId
import com.tbank.smartbudget.data.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация репозитория авторизации.
 * Разделяет вызовы на публичные (AuthApi) и защищенные (UserApi).
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<User> = safeApiCall {
        val response = authApi.login(LoginRequest(email, password))

        sessionManager.saveAuthData(
            token = response.token,
            userId = response.userId,
            name = response.name
        )

        User(
            id = UserId(response.userId),
            name = response.name,
            email = email,
            token = response.token
        )
    }

    override suspend fun register(email: String, password: String, name: String): AppResult<User> = safeApiCall {
        val response = authApi.register(RegisterRequest(email, password, name))

        sessionManager.saveAuthData(
            token = response.token,
            userId = response.userId,
            name = response.name
        )

        User(
            id = UserId(response.userId),
            name = response.name,
            email = email,
            token = response.token
        )
    }

    override suspend fun getProfile(): AppResult<User> = safeApiCall {
        val dto = userApi.getProfile()
        User(
            id = UserId(dto.id),
            name = dto.name,
            email = dto.email,
            avatarUrl = dto.avatarUrl
        )
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override suspend fun checkUserExistence(email: String): AppResult<Boolean> = safeApiCall {
        val response = authApi.checkEmail(CheckEmailRequest(email))
        response.registered
    }
}