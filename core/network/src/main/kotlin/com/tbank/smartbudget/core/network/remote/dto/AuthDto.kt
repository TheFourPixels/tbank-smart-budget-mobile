package com.tbank.smartbudget.core.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// --- Requests ---
@Serializable
data class AuthRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)
@Serializable
data class RegisterRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("name") val name: String
)
@Serializable
data class UpdateProfileRequest(
    @SerialName("name") val name: String,
    @SerialName("avatarUrl") val avatarUrl: String?
)
@Serializable
data class CheckEmailRequest(
    @SerialName("email") val email: String
)

// --- Responses ---
@Serializable
data class AuthResponse(
    @SerialName("token") val token: String,
    @SerialName("userId") val userId: Long,
    @SerialName("name") val name: String
)
@Serializable
data class UserProfileDto(
    @SerialName("id") val id: Long,
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("avatarUrl") val avatarUrl: String?
)
@Serializable
data class CheckEmailResponse(
    @SerialName("registered") val registered: Boolean
)