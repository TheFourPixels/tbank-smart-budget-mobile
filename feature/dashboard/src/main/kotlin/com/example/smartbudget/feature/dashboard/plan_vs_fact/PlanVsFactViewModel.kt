package com.example.smartbudget.feature.dashboard.plan_vs_fact

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.model.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.TransactionType
import com.tbank.smartbudget.data.domain.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PlanVsFactViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : BaseViewModel<PlanVsFactUiState, PlanVsFactIntent, PlanVsFactEffect>(
    PlanVsFactUiState(isLoading = true)
) {

    private val now = LocalDate.now()
    private val currentYear = now.year
    private val currentMonth = now.monthValue

    init {
        onIntent(PlanVsFactIntent.LoadData)
    }

    override fun onIntent(intent: PlanVsFactIntent) {
        when (intent) {
            PlanVsFactIntent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val date = LocalDate.of(currentYear, currentMonth, 1)
            val formattedPeriod = date.format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru")))
                .replaceFirstChar { it.uppercase() }

            dashboardRepository.getDashboardSummary(currentMonth, currentYear)
                .onSuccess { data ->
                    val categories = data.categoryStats
                    val totalPlanVal = data.spendingLimit ?: categories.sumOf { it.limitAmount }
                    val totalFactVal = data.totalSpent

                    val historyMap = data.recentTransactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .groupBy { it.date.toLocalDate() }
                        .mapValues { it.value.sumOf { tx -> tx.amount }.toFloat() }

                    val historyPoints = mutableListOf<Float>()
                    var cumulativeSum = 0f
                    val firstDay = LocalDate.of(currentYear, currentMonth, 1)
                    val lastDay = LocalDate.now()

                    var d = firstDay
                    while (!d.isAfter(lastDay)) {
                        cumulativeSum += historyMap[d] ?: 0f
                        historyPoints.add(cumulativeSum)
                        d = d.plusDays(1)
                    }

                    val diffPercent = if (totalPlanVal > 0) ((totalFactVal - totalPlanVal) / totalPlanVal) * 100 else 0.0
                    val diffLabel = "${if (diffPercent > 0) "+" else ""}${"%.1f".format(diffPercent)}%"

                    val uiCategories = categories.map { cat ->
                        val catProgress = if (cat.limitAmount > 0) (cat.spentAmount / cat.limitAmount).toFloat() else 0f
                        PlanVsFactCategoryUi(
                            id = cat.id,
                            name = cat.name,
                            iconRes = cat.iconRes,
                            color = Color(CategoryColorMapper.getColorForId(cat.id.value)),
                            planAmount = formatMoney(cat.limitAmount),
                            factAmount = formatMoney(cat.spentAmount),
                            progress = catProgress.coerceIn(0f, 1f),
                            progressColor = if (catProgress > 1.0f) Color(0xFFE53935) else Color(0xFF43A047)
                        )
                    }

                    updateState {
                        copy(
                            isLoading = false,
                            totalPlan = formatMoney(totalPlanVal),
                            totalFact = formatMoney(totalFactVal),
                            planValue = totalPlanVal,
                            factValue = totalFactVal,
                            dailyLimit = totalPlanVal.toFloat(),
                            percentageDiffLabel = diffLabel,
                            expenseHistory = historyPoints,
                            categories = uiCategories,
                            periodName = formattedPeriod
                        )
                    }
                }
                .onFailure { updateState { copy(isLoading = false) } }
        }
    }

    private fun formatMoney(amount: Double): String = "%,.0f ₽".format(amount).replace(',', ' ')
}