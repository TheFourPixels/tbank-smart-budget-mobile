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

            // Загрузка сводки
            val summaryResult = getBudgetSummaryUseCase.execute()

            summaryResult.onSuccess { summary ->
                // Форматирование данных Domain-слоя в UI-модель
                val summaryUi = BudgetSummaryUi(
                    totalIncome = "%,.2f ₽".format(summary.totalIncome),
                    totalLimit = "%,.2f ₽".format(summary.totalLimit),
                    freeFunds = "%,.2f ₽".format(summary.freeFunds)
                )
                _uiState.update { it.copy(summary = summaryUi) }
            }

            // Загрузка категорий (имитация)
            val categoriesResult = getCategoryDetailsUseCase.execute(budgetId = 1L)

            categoriesResult.onSuccess { domainCategories ->
                // Преобразование Domain-моделей в UI-модели (CategoryUi)
                val uiCategories = domainCategories.map { domainCategory ->
                    CategoryUi(
                        id = domainCategory.id,
                        name = domainCategory.name,
                        iconRes = domainCategory.iconRes,
                        color = domainCategory.color,
                        spentValue = "%,.0f".format(domainCategory.spentAmount),
                        limitValue = "%,.0f".format(domainCategory.limitAmount),
                        progress = (domainCategory.spentAmount / domainCategory.limitAmount).toFloat().coerceIn(0f, 1f)
                    )
                }

                _uiState.update {
                    it.copy(
                        categories = uiCategories,
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun onPeriodToggle(newPeriod: BudgetPeriod) {
        _uiState.update { it.copy(period = newPeriod) }
        // В реальном приложении здесь нужно перезагрузить loadData() с новым периодом
    }

    // Функция для перехода к редактированию
    fun onEditClicked() {

        // ... логика навигации ...
    }
}
