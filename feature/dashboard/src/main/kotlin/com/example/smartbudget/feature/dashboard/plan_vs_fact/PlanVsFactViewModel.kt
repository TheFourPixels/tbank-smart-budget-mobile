package com.example.smartbudget.feature.dashboard.plan_vs_fact

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.core.ui.common.CategoryColorMapper
import com.tbank.smartbudget.data.domain.usecase.GetCategoryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PlanVsFactViewModel @Inject constructor(
    private val getCategoryDetailsUseCase: GetCategoryDetailsUseCase
) : BaseViewModel<PlanVsFactUiState, PlanVsFactIntent, PlanVsFactEffect>(
    PlanVsFactUiState(isLoading = true)
) {

    private val currentYear = 2026
    private val currentMonth = 1

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

            getCategoryDetailsUseCase.execute(budgetId = 1L)
                .onSuccess { categories ->
                    val totalPlanVal = categories.sumOf { it.limitAmount }
                    val totalFactVal = categories.sumOf { it.spentAmount }

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
                            percentageDiffLabel = diffLabel,
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