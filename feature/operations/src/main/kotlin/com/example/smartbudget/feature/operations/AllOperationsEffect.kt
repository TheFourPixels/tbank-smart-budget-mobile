package com.example.smartbudget.feature.operations

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface AllOperationsEffect : UiEffect {
    data object NavigateBack : AllOperationsEffect
    data object NavigateToSearch : AllOperationsEffect
}