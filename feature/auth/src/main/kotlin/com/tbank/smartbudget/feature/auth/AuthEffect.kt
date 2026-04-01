package com.tbank.smartbudget.feature.auth

sealed interface AuthEffect {
    data object NavigateNext : AuthEffect
    data class ShowError(val message: String) : AuthEffect
}