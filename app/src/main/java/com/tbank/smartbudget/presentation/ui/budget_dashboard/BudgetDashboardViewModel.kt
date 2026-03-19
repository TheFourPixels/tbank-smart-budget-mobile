package com.tbank.smartbudget.presentation.ui.budget_dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.usecase.GetBudgetDashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetDashboardViewModel @Inject constructor(
    private val getDashboardUseCase: GetBudgetDashboardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetDashboardUiState(isLoading = true))
    val uiState: StateFlow<BudgetDashboardUiState> = _uiState.asStateFlow()

    private val currentYear = 2025
    private val currentMonth = 12

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getDashboardUseCase.execute(currentYear, currentMonth)
                .onSuccess { summary ->
                    // Расчет прогресса
                    val progress = if (summary.totalLimit > 0) {
                        (summary.totalSpent / summary.totalLimit).toFloat()
                    } else 0f

                    // Цвет прогресса
                    val color = when {
                        progress > 1.0f -> 0xFFE53935 // Красный (превышение)
                        progress > 0.9f -> 0xFFFF7043 // Оранжевый
                        progress > 0.5f -> 0xFFFBC02D // Желтый
                        else -> 0xFF43A047 // Зеленый
                    }


                    val daysRemaining = 30
                    val dailySafe = if (daysRemaining > 0) summary.freeFunds / daysRemaining else 0.0

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalLimit = formatMoney(summary.totalLimit),
                            totalSpent = formatMoney(summary.totalSpent),
                            remainingAmount = formatMoney(summary.freeFunds),
                            progress = progress.coerceIn(0f, 1f),
                            progressColor = color,
                            daysLeft = daysRemaining,
                            dailyBudget = formatMoney(dailySafe),
                            periodDescription = summary.period
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, error = "Ошибка загрузки данных") }
                }
        }
    }

    private fun formatMoney(amount: Double): String {
        return "%,.0f ₽".format(amount).replace(',', ' ')
    }
}