package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.datastore.SessionManager
import com.tbank.smartbudget.core.network.remote.api.AuthApi
import com.tbank.smartbudget.core.network.remote.dto.CheckEmailRequest
import com.tbank.smartbudget.core.network.remote.dto.LoginRequest
import com.tbank.smartbudget.core.network.remote.dto.RegisterRequest
import com.tbank.smartbudget.data.domain.model.User
import com.tbank.smartbudget.data.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, password))

            sessionManager.saveAuthData(
                token = response.token,
                userId = response.userId,
                name = response.name
            )

            val user = User(
                id = response.userId,
                name = response.name,
                email = email,
                token = response.token
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<User> {
        return try {
            val response = api.register(RegisterRequest(email, password, name))

            sessionManager.saveAuthData(
                token = response.token,
                userId = response.userId,
                name = response.name
            )

            val user = User(
                id = response.userId,
                name = response.name,
                email = email,
                token = response.token
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<User> {
        return try {
            val dto = api.getProfile()
            val user = User(
                id = dto.id,
                name = dto.name,
                email = dto.email,
                avatarUrl = dto.avatarUrl
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override suspend fun checkUserExistence(email: String): Result<Boolean> {
        return try {
            val response = api.checkEmail(CheckEmailRequest(email))
            Result.success(response.registered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}