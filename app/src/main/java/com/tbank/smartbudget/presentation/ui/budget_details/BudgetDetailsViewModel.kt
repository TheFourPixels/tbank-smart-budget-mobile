package com.tbank.smartbudget.presentation.ui.budget_details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BudgetDetailsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetDetailsUiState())
    val uiState: StateFlow<BudgetDetailsUiState> = _uiState.asStateFlow()

    fun onLimitNotificationToggle(enabled: Boolean) {
        _uiState.update { it.copy(isLimitNotificationEnabled = enabled) }
    }

    fun onOperationNotificationToggle(enabled: Boolean) {
        _uiState.update { it.copy(isOperationNotificationEnabled = enabled) }
    }
}