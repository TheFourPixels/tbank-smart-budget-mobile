package com.tbank.smartbudget.feature.auth

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface AuthIntent : UiIntent {
    data class OnEmailChanged(val email: String) : AuthIntent
    data object OnEmailSubmit : AuthIntent
    data class OnPasswordChanged(val password: String) : AuthIntent
    data object OnPasswordSubmit : AuthIntent
    data class OnPinDigitEntered(val digit: Char) : AuthIntent
    data object OnPinBackspace : AuthIntent
    data object ClearError : AuthIntent
    data class InitAuthData(val email: String, val isUserExisting: Boolean, val userName: String?) : AuthIntent
}