package com.tbank.smartbudget.core.network.remote.dto

import com.google.gson.annotations.SerializedName

// --- Requests ---

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)

data class UpdateProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("avatarUrl") val avatarUrl: String?
)

data class CheckEmailRequest(
    @SerializedName("email") val email: String
)

// --- Responses ---

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: Long,
    @SerializedName("name") val name: String
)

data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatarUrl") val avatarUrl: String?
)

data class CheckEmailResponse(
    @SerializedName("registered") val registered: Boolean
)