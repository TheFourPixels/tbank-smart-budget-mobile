package com.example.smartbudget.feature.dashboard

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.usecase.GetBudgetDashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetDashboardViewModel @Inject constructor(
    private val getDashboardUseCase: GetBudgetDashboardUseCase
) : BaseViewModel<BudgetDashboardUiState, BudgetDashboardIntent, BudgetDashboardEffect>(
    BudgetDashboardUiState(isLoading = true)
) {

    private val currentYear = 2025
    private val currentMonth = 12

    init {
        onIntent(BudgetDashboardIntent.LoadDashboard)
    }

    override fun onIntent(intent: BudgetDashboardIntent) {
        when (intent) {
            BudgetDashboardIntent.LoadDashboard -> loadDashboard()
            is BudgetDashboardIntent.OnChartTypeSelected -> handleChartSelection(intent.chartType)
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            getDashboardUseCase.execute(currentYear, currentMonth)
                .onSuccess { summary ->
                    val progress = if (summary.totalLimit > 0) (summary.totalSpent / summary.totalLimit).toFloat() else 0f

                    val color = when {
                        progress > 1.0f -> 0xFFE53935
                        progress > 0.9f -> 0xFFFF7043
                        progress > 0.5f -> 0xFFFBC02D
                        else -> 0xFF43A047
                    }

                    updateState {
                        copy(
                            isLoading = false,
                            totalLimit = formatMoney(summary.totalLimit),
                            totalSpent = formatMoney(summary.totalSpent),
                            remainingAmount = formatMoney(summary.freeFunds),
                            progress = progress.coerceIn(0f, 1f),
                            progressColor = color,
                            daysLeft = 30,
                            dailyBudget = formatMoney(if (30 > 0) summary.freeFunds / 30 else 0.0),
                            periodDescription = summary.period
                        )
                    }
                }
                .onFailure {
                    updateState { copy(isLoading = false, error = "Ошибка загрузки данных") }
                }
        }
    }

    private fun handleChartSelection(chartType: String) {
        when (chartType) {
            "plan_vs_fact" -> sendEffect(BudgetDashboardEffect.NavigateToPlanVsFact)
            "categories_dashboard" -> sendEffect(BudgetDashboardEffect.NavigateToCategoriesDashboard)
        }
    }

    private fun formatMoney(amount: Double): String = "%,.0f ₽".format(amount).replace(',', ' ')
}