package com.tbank.smartbudget.feature.auth

import androidx.compose.runtime.Immutable

@Immutable
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val pinCode: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,

    val userName: String? = null,
    val isUserExisting: Boolean = true, // true = вход, false = регистрация

    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isPinComplete: Boolean = false
)