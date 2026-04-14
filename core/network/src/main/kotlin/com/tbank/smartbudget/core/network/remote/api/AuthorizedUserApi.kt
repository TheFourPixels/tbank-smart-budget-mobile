package com.tbank.smartbudget.core.network.remote.api

import com.tbank.smartbudget.core.network.remote.dto.UpdateProfileRequest
import com.tbank.smartbudget.core.network.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

/**
 * Эндпоинты пользователя (требуют заголовок Authorization).
 */
interface AuthorizedUserApi {
    @GET("api/v1/profile")
    suspend fun getProfile(): UserDto

    @PUT("api/v1/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UserDto
}