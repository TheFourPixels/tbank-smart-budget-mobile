package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 * Публичные эндпоинты (без токена).
 */
interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/v1/auth/check-email")
    suspend fun checkEmail(@Body request: CheckEmailRequest): CheckEmailResponse
}

/**
 * Эндпоинты пользователя (требуют заголовок Authorization).
 */
interface UserApi {
    @GET("api/v1/profile")
    suspend fun getProfile(): UserDto

    @PUT("api/v1/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UserDto
}