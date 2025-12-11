package com.tbank.smartbudget.presentation.ui.budget_edit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BudgetEditViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetEditUiState())
    val uiState: StateFlow<BudgetEditUiState> = _uiState.asStateFlow()

    fun onPeriodSelected(index: Int) {
        _uiState.update { it.copy(selectedPeriodIndex = index) }
    }

    // Методы для изменения суммы и добавления категорий будут здесь
}