package com.tbank.smartbudget.presentation.ui.budget_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.model.BudgetLimitData
import com.tbank.smartbudget.domain.model.BudgetLimitType
import com.tbank.smartbudget.domain.usecase.GetBudgetDetailsUseCase
import com.tbank.smartbudget.domain.usecase.SaveBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetEditViewModel @Inject constructor(
    private val getBudgetDetailsUseCase: GetBudgetDetailsUseCase,
    private val saveBudgetUseCase: SaveBudgetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetEditUiState(isLoading = true))
    val uiState: StateFlow<BudgetEditUiState> = _uiState.asStateFlow()

    private val currentYear = 2025
    private val currentMonth = 12

    init {
        loadBudget()
    }

    private fun loadBudget() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getBudgetDetailsUseCase.execute(currentYear, currentMonth)
                .onSuccess { budget ->
                    val uiCategories = budget.limits.map { limit ->
                        EditCategoryUi(
                            id = limit.categoryId,
                            name = limit.categoryName,
                            limitValue = formatValue(limit.limitValue),
                            limitType = limit.limitType,
                            color = limit.color
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            amount = formatValue(budget.totalIncome),
                            categories = uiCategories,
                            // Определяем режим по большинству категорий или дефолт
                            isPercentMode = uiCategories.any { it.limitType == BudgetLimitType.PERCENT }
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    // *** НОВАЯ ЛОГИКА: Переключение глобального режима ***
    fun onGlobalLimitTypeToggle() {
        val currentState = _uiState.value
        val newModeIsPercent = !currentState.isPercentMode
        val totalIncome = currentState.amount.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0

        val updatedCategories = currentState.categories.map { cat ->
            val currentValue = cat.limitValue.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0

            val newValue = if (newModeIsPercent) {
                // Конвертация Сумма -> Процент
                // Формула: (Сумма / Доход) * 100
                if (totalIncome != 0.0) (currentValue / totalIncome) * 100 else 0.0
            } else {
                // Конвертация Процент -> Сумма
                // Формула: (Процент / 100) * Доход
                (currentValue / 100) * totalIncome
            }

            cat.copy(
                limitValue = formatValue(newValue),
                limitType = if (newModeIsPercent) BudgetLimitType.PERCENT else BudgetLimitType.AMOUNT
            )
        }

        _uiState.update {
            it.copy(
                isPercentMode = newModeIsPercent,
                categories = updatedCategories
            )
        }
    }

    fun onPeriodSelected(index: Int) {
        _uiState.update { it.copy(selectedPeriodIndex = index) }
    }

    fun onAmountChanged(newAmount: String) {
        if (newAmount.all { it.isDigit() || it == '.' || it == ',' }) {
            _uiState.update { it.copy(amount = newAmount) }
        }
    }

    fun onCategoryLimitChanged(categoryId: Long, newValue: String) {
        // Разрешаем ввод цифр, точки и запятой
        if (newValue.all { it.isDigit() || it == '.' || it == ',' }) {
            _uiState.update { state ->
                val updatedCategories = state.categories.map {
                    if (it.id == categoryId) it.copy(limitValue = newValue) else it
                }
                state.copy(categories = updatedCategories)
            }
        }
    }

    fun onCategoryTypeToggle(categoryId: Long) {
        _uiState.update { state ->
            val updatedCategories = state.categories.map {
                if (it.id == categoryId) {
                    val newType = if (it.limitType == BudgetLimitType.PERCENT) BudgetLimitType.AMOUNT else BudgetLimitType.PERCENT
                    it.copy(limitType = newType)
                } else it
            }
            state.copy(categories = updatedCategories)
        }
    }

    fun onSaveClicked() {
        val currentState = _uiState.value
        val income = currentState.amount.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0

        val limitsData = currentState.categories.mapNotNull { uiCat ->
            val value = uiCat.limitValue.replace(" ", "").replace(",", ".").toDoubleOrNull()
            if (value != null) {
                BudgetLimitData(
                    categoryId = uiCat.id,
                    limitValue = value,
                    limitType = if (uiCat.limitType == BudgetLimitType.PERCENT) "PERCENT" else "AMOUNT"
                )
            } else null
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            saveBudgetUseCase.execute(currentYear, currentMonth, income, limitsData)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, isSavedSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSaving = false, error = e.message) }
                }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    fun onSuccessShown() {
        _uiState.update { it.copy(isSavedSuccess = false) }
    }

    private fun formatValue(value: Double): String {
        // Если число целое, убираем .0
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format("%.2f", value).replace('.', ',')
        }
    }
}