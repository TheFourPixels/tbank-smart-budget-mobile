package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.AuthResponse
import com.tbank.smartbudget.core.network.remote.dto.CheckEmailRequest
import com.tbank.smartbudget.core.network.remote.dto.CheckEmailResponse
import com.tbank.smartbudget.core.network.remote.dto.LoginRequest
import com.tbank.smartbudget.core.network.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Публичные эндпоинты (без токена).
 */
interface UnauthorizedApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/v1/auth/check-email")
    suspend fun checkEmail(@Body request: CheckEmailRequest): CheckEmailResponse
}