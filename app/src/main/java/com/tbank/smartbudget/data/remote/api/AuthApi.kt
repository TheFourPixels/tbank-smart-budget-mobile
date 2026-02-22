package com.tbank.smartbudget.data.remote.api

import com.tbank.smartbudget.data.remote.dto.AuthResponse
import com.tbank.smartbudget.data.remote.dto.CheckEmailRequest
import com.tbank.smartbudget.data.remote.dto.CheckEmailResponse
import com.tbank.smartbudget.data.remote.dto.LoginRequest
import com.tbank.smartbudget.data.remote.dto.RegisterRequest
import com.tbank.smartbudget.data.remote.dto.UpdateProfileRequest
import com.tbank.smartbudget.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/v1/profile")
    suspend fun getProfile(): UserDto

    @PUT("api/v1/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UserDto

    @POST("api/v1/auth/check-email")
    suspend fun checkEmail(@Body request: CheckEmailRequest): CheckEmailResponse
}