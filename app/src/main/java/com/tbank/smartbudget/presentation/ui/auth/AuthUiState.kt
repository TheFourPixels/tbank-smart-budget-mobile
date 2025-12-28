package com.tbank.smartbudget.presentation.ui.auth

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val pinCode: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,

    // Результат проверки email
    val userName: String? = null, // Имя пользователя, если найден
    val isUserExisting: Boolean = false, // true = вход, false = регистрация

    // Флаги валидации
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isPinComplete: Boolean = false
)