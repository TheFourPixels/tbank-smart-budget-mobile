package com.example.smartbudget.feature.dashboard

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.model.TransactionType
import com.tbank.smartbudget.data.domain.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BudgetDashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : BaseViewModel<BudgetDashboardUiState, BudgetDashboardIntent, BudgetDashboardEffect>(
    BudgetDashboardUiState(isLoading = true)
) {

    private val now = LocalDate.now()
    private val currentYear = now.year
    private val currentMonth = now.monthValue

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

            dashboardRepository.getDashboardSummary(currentMonth, currentYear)
                .onSuccess { data ->
                    val totalLimit = data.totalSpent + data.remainingBudget
                    val progress = if (totalLimit > 0) (data.totalSpent / totalLimit).toFloat() else 0f

                    val color = when {
                        progress > 1.0f -> 0xFFE53935
                        progress > 0.9f -> 0xFFFF7043
                        progress > 0.5f -> 0xFFFBC02D
                        else -> 0xFF43A047
                    }

                    val monthName = now.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
                        .replaceFirstChar { it.uppercase() }

                    val daysInMonth = now.month.length(now.isLeapYear)
                    val daysLeft = (daysInMonth - now.dayOfMonth).coerceAtLeast(1)

                    // Generate real-ish history data based on income and spent
                    val historyPoints = if (totalLimit > 0) {
                        val points = mutableListOf<Float>()
                        var currentBalance = data.totalIncome
                        points.add(1.0f) // Start at 100%
                        
                        // We use recent transactions to show a downward trend
                        data.recentTransactions.filter { it.type == TransactionType.EXPENSE }
                            .sortedBy { it.date }
                            .takeLast(7)
                            .forEach { tx ->
                                currentBalance -= tx.amount
                                points.add((currentBalance / data.totalIncome).toFloat().coerceIn(0f, 1f))
                            }
                        
                        if (points.size < 5) {
                           // If not enough transactions, add a trend point
                           points.add((data.remainingBudget / data.totalIncome).toFloat().coerceIn(0f, 1f))
                        }
                        points
                    } else listOf(0f)

                    updateState {
                        copy(
                            isLoading = false,
                            totalLimit = formatMoney(totalLimit),
                            totalSpent = formatMoney(data.totalSpent),
                            totalIncome = formatMoney(data.totalIncome),
                            remainingAmount = formatMoney(data.remainingBudget),
                            progress = progress.coerceIn(0f, 1f),
                            progressColor = color,
                            daysLeft = daysLeft,
                            dailyBudget = formatMoney(data.remainingBudget / daysLeft),
                            periodDescription = monthName,
                            historyData = historyPoints,
                            recentTransactions = data.recentTransactions,
                            activeGoals = data.activeGoals,
                            categoryStats = data.categoryStats
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
