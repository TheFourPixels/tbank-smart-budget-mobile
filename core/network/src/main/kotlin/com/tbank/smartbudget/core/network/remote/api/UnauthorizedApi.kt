package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.AuthRequest
import com.tbank.smartbudget.core.network.remote.dto.AuthResponse
import com.tbank.smartbudget.core.network.remote.dto.CheckEmailRequest
import com.tbank.smartbudget.core.network.remote.dto.CheckEmailResponse
import com.tbank.smartbudget.core.network.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Публичные эндпоинты (без токена).
 */
interface UnauthorizedApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("api/v1/auth/check-email")
    suspend fun checkEmail(@Body request: CheckEmailRequest): CheckEmailResponse

    @POST("api/v1/profile/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String)

    @POST("api/v1/profile/reset-password")
    suspend fun resetPassword(@Query("token") token: String, @Body request: AuthRequest)
}