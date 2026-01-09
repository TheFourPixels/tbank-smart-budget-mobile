package com.tbank.smartbudget.presentation.ui.budget_dashboard.plan_vs_fact

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.usecase.GetCategoryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class PlanVsFactViewModel @Inject constructor(
    private val getCategoryDetailsUseCase: GetCategoryDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlanVsFactUiState(isLoading = true))
    val uiState: StateFlow<PlanVsFactUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Используем ID бюджета = 1 (хардкод для примера)
            getCategoryDetailsUseCase.execute(budgetId = 1L)
                .onSuccess { categories ->
                    val totalPlanVal = categories.sumOf { it.limitAmount }
                    val totalFactVal = categories.sumOf { it.spentAmount }

                    // Расчет процента разницы для бабла
                    val diffPercent = if (totalPlanVal > 0) {
                        ((totalFactVal - totalPlanVal) / totalPlanVal) * 100
                    } else 0.0

                    val sign = if (diffPercent > 0) "+" else ""
                    val diffLabel = "$sign%.1f%%".format(diffPercent)

                    // Маппим категории (оставляем старую логику для списка, если он понадобится ниже)
                    val uiCategories = categories.map { cat ->
                        val catProgress = if (cat.limitAmount > 0) (cat.spentAmount / cat.limitAmount).toFloat() else 0f
                        PlanVsFactCategoryUi(
                            id = cat.id,
                            name = cat.name,
                            iconRes = cat.iconRes,
                            color = Color(cat.color),
                            planAmount = formatMoney(cat.limitAmount),
                            factAmount = formatMoney(cat.spentAmount),
                            progress = catProgress.coerceIn(0f, 1f),
                            progressColor = getProgressColor(catProgress)
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalPlan = formatMoney(totalPlanVal),
                            totalFact = formatMoney(totalFactVal),
                            planValue = totalPlanVal,
                            factValue = totalFactVal,
                            percentageDiffLabel = diffLabel,
                            categories = uiCategories
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun getProgressColor(progress: Float): Color {
        return when {
            progress > 1.0f -> Color(0xFFE53935)
            progress > 0.9f -> Color(0xFFFF7043)
            else -> Color(0xFF43A047)
        }
    }

    private fun formatMoney(amount: Double): String {
        return "%,.0f ₽".format(amount).replace(',', ' ')
    }
}