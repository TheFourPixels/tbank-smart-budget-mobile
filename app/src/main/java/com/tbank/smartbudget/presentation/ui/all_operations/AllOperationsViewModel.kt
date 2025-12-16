package com.tbank.smartbudget.presentation.ui.all_operations

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AllOperationsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AllOperationsUiState())
    val uiState: StateFlow<AllOperationsUiState> = _uiState.asStateFlow()

    fun onPeriodChanged(periodType: PeriodType) {
        _uiState.update { it.copy(periodType = periodType) }
    }

    // Здесь можно добавить методы для фильтрации и поиска
}