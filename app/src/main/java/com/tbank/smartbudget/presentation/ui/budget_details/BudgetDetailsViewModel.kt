package com.tbank.smartbudget.presentation.ui.budget_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.model.BudgetLimitType
import com.tbank.smartbudget.domain.usecase.GetBudgetDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetDetailsViewModel @Inject constructor(
    private val getBudgetDetailsUseCase: GetBudgetDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetDetailsUiState())
    val uiState: StateFlow<BudgetDetailsUiState> = _uiState.asStateFlow()

    private val currentYear = 2025
    private val currentMonth = 12

    init {
        loadBudgetDetails()
    }

    fun loadBudgetDetails() {
        viewModelScope.launch {
            getBudgetDetailsUseCase.execute(currentYear, currentMonth)
                .onSuccess { budget ->
                    val totalIncome = budget.totalIncome
                    val totalLimitAmount = budget.limits.sumOf { limit ->
                        if (limit.limitType == BudgetLimitType.PERCENT) {
                            totalIncome * (limit.limitValue / 100)
                        } else {
                            limit.limitValue
                        }
                    }
                    val freeFunds = totalIncome - totalLimitAmount

                    _uiState.update { currentState ->
                        currentState.copy(
                            budgetName = "Кубышка",
                            currentBalance = formatMoney(totalIncome),
                            periodDescription = "На ${budget.period}", // Используем сохраненный период
                            income = formatMoney(totalIncome),
                            expenseLimit = formatMoney(totalLimitAmount),
                            freeFunds = formatMoney(freeFunds)
                        )
                    }
                }
        }
    }

    fun onLimitNotificationToggle(enabled: Boolean) {
        _uiState.update { it.copy(isLimitNotificationEnabled = enabled) }
    }

    fun onOperationNotificationToggle(enabled: Boolean) {
        _uiState.update { it.copy(isOperationNotificationEnabled = enabled) }
    }

    private fun formatMoney(amount: Double): String {
        return "%,.0f ₽".format(amount).replace(',', ' ')
    }
}