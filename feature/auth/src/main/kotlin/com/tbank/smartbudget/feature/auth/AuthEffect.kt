package com.tbank.smartbudget.feature.auth

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface AuthEffect : UiEffect {
    data object NavigateNext : AuthEffect
    data class ShowError(val message: String) : AuthEffect
}