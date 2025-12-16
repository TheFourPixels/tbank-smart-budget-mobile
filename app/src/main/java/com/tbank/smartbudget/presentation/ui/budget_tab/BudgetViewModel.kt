package com.tbank.smartbudget.presentation.ui.budget_tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.model.BudgetPeriod
import com.tbank.smartbudget.domain.usecase.GetBudgetSummaryUseCase
import com.tbank.smartbudget.domain.usecase.GetCategoryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetSummaryUi(
    val totalIncome: String,
    val totalLimit: String,
    val totalSpent: String,
    val freeFunds: String
)

data class CategoryUi(
    val id: Long,
    val name: String,
    val iconRes: Int,
    val color: Long,
    val spentValue: String,
    val limitValue: String,
    val progress: Float
)

data class BudgetUiState(
    val period: BudgetPeriod = BudgetPeriod.MONTH,
    val userName: String = "Валерия",
    val budgetName: String = "Кубышка",
    val budgetTerm: String = "2 месяца", // Отображается на главной карточке
    val summary: BudgetSummaryUi? = null,
    val categories: List<CategoryUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    private val getCategoryDetailsUseCase: GetCategoryDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState(isLoading = true))
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val summaryResult = getBudgetSummaryUseCase.execute()
            val categoriesResult = getCategoryDetailsUseCase.execute(budgetId = 1L)

            if (summaryResult.isSuccess && categoriesResult.isSuccess) {
                val summary = summaryResult.getOrNull()!!
                val domainCategories = categoriesResult.getOrNull()!!

                val summaryUi = BudgetSummaryUi(
                    totalIncome = formatMoney(summary.totalIncome),
                    totalLimit = formatMoney(summary.totalLimit),
                    totalSpent = formatMoney(summary.totalSpent),
                    freeFunds = formatMoney(summary.freeFunds)
                )

                val uiCategories = domainCategories.map { domainCategory ->
                    CategoryUi(
                        id = domainCategory.id,
                        name = domainCategory.name,
                        iconRes = domainCategory.iconRes,
                        color = domainCategory.color,
                        spentValue = formatMoney(domainCategory.spentAmount),
                        limitValue = formatMoney(domainCategory.limitAmount),
                        progress = if (domainCategory.limitAmount > 0)
                            (domainCategory.spentAmount / domainCategory.limitAmount).toFloat().coerceIn(0f, 1f)
                        else 0f
                    )
                }

                _uiState.update {
                    it.copy(
                        budgetTerm = summary.period, // Обновляем срок из сохраненного значения
                        summary = summaryUi,
                        categories = uiCategories,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(error = "Ошибка загрузки", isLoading = false) }
            }
        }
    }

    private fun formatMoney(amount: Double): String {
        return "%,.0f ₽".format(amount).replace(',', ' ')
    }
}