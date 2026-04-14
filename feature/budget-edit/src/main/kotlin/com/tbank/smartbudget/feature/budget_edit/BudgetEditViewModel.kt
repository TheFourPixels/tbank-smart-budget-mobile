package com.tbank.smartbudget.feature.budget_edit

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.core.ui.common.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.BudgetLimitData
import com.tbank.smartbudget.data.domain.model.BudgetLimitType
import com.tbank.smartbudget.data.domain.usecase.DeleteBudgetUseCase
import com.tbank.smartbudget.data.domain.usecase.GetBudgetDetailsUseCase
import com.tbank.smartbudget.data.domain.usecase.SaveBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetEditViewModel @Inject constructor(
    private val getBudgetDetailsUseCase: GetBudgetDetailsUseCase,
    private val saveBudgetUseCase: SaveBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase
) : BaseViewModel<BudgetEditUiState, BudgetEditIntent, BudgetEditEffect>(
    BudgetEditUiState(isLoading = true)
) {

    private val currentYear = 2025
    private val currentMonth = 12

    init {
        onIntent(BudgetEditIntent.LoadBudget)
    }

    override fun onIntent(intent: BudgetEditIntent) {
        when (intent) {
            BudgetEditIntent.LoadBudget -> loadBudget()
            BudgetEditIntent.RefreshCategories -> refreshCategories()
            BudgetEditIntent.ToggleGlobalLimitType -> handleGlobalLimitToggle()
            is BudgetEditIntent.OnPeriodSelected -> updateState { copy(selectedPeriodIndex = intent.index) }
            is BudgetEditIntent.OnAmountChanged -> handleAmountChange(intent.newAmount)
            is BudgetEditIntent.OnCategoryLimitChanged -> handleCategoryLimitChange(intent.categoryId, intent.newValue)
            is BudgetEditIntent.OnCategoryTypeToggle -> handleCategoryTypeToggle(intent.categoryId)
            BudgetEditIntent.OnSaveClicked -> saveBudget()
            BudgetEditIntent.OnDeleteClicked -> deleteBudget()
            BudgetEditIntent.ClearError -> updateState { copy(error = null) }
            BudgetEditIntent.ResetSuccess -> updateState { copy(isSavedSuccess = false) }
        }
    }

    private fun loadBudget() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getBudgetDetailsUseCase(currentYear, currentMonth)
                .onSuccess { budget ->
                    val uiCategories = budget.limits.map { limit ->
                        EditCategoryUi(
                            id = limit.categoryId,
                            name = limit.categoryName,
                            limitValue = formatValue(limit.limitValue),
                            limitType = limit.limitType,
                            color = CategoryColorMapper.getColorForId(limit.categoryId.value)
                        )
                    }

                    val periodIndex = currentState.periods.indexOf(budget.period).coerceAtLeast(0)

                    updateState {
                        copy(
                            isLoading = false,
                            amount = formatValue(budget.totalIncome),
                            categories = uiCategories,
                            selectedPeriodIndex = periodIndex,
                            isPercentMode = uiCategories.any { it.limitType == BudgetLimitType.PERCENT }
                        )
                    }
                }
                .onFailure { e ->
                    updateState { copy(isLoading = false, error = e.message) }
                }
        }
    }

    private fun refreshCategories() {
        viewModelScope.launch {
            getBudgetDetailsUseCase(currentYear, currentMonth)
                .onSuccess { budget ->
                    updateState {
                        val mergedCategories = budget.limits.map { serverLimit ->
                            categories.find { it.id == serverLimit.categoryId } ?: EditCategoryUi(
                                id = serverLimit.categoryId,
                                name = serverLimit.categoryName,
                                limitValue = formatValue(serverLimit.limitValue),
                                limitType = serverLimit.limitType,
                                color = CategoryColorMapper.getColorForId(serverLimit.categoryId.value)
                            )
                        }
                        copy(categories = mergedCategories)
                    }
                }
        }
    }

    private fun handleGlobalLimitToggle() {
        updateState {
            val newModeIsPercent = !isPercentMode
            val totalIncome = amount.parseToDouble()

            val updatedCategories = categories.map { cat ->
                val currentValue = cat.limitValue.parseToDouble()
                val newValue = if (newModeIsPercent) {
                    if (totalIncome != 0.0) (currentValue / totalIncome) * 100 else 0.0
                } else {
                    (currentValue / 100) * totalIncome
                }
                cat.copy(
                    limitValue = formatValue(newValue),
                    limitType = if (newModeIsPercent) BudgetLimitType.PERCENT else BudgetLimitType.AMOUNT
                )
            }
            copy(isPercentMode = newModeIsPercent, categories = updatedCategories)
        }
    }

    private fun handleAmountChange(newAmount: String) {
        if (newAmount.isValidInput()) {
            updateState { copy(amount = newAmount) }
        }
    }

    private fun handleCategoryLimitChange(categoryId: Long, newValue: String) {
        if (newValue.isValidInput()) {
            updateState {
                val updated = categories.map {
                    if (it.id.value == categoryId) it.copy(limitValue = newValue) else it
                }
                copy(categories = updated)
            }
        }
    }

    private fun handleCategoryTypeToggle(categoryId: Long) {
        updateState {
            val updated = categories.map {
                if (it.id.value == categoryId) {
                    val newType = if (it.limitType == BudgetLimitType.PERCENT)
                        BudgetLimitType.AMOUNT else BudgetLimitType.PERCENT
                    it.copy(limitType = newType)
                } else it
            }
            copy(categories = updated)
        }
    }

    private fun saveBudget() {
        val s = currentState
        val income = s.amount.parseToDouble()
        val selectedPeriod = s.periods.getOrElse(s.selectedPeriodIndex) { "1 мес" }

        val limitsData = s.categories.mapNotNull { uiCat ->
            uiCat.limitValue.parseToDoubleOrNull()?.let { value ->
                BudgetLimitData(
                    categoryId = uiCat.id.value,
                    limitValue = value,
                    limitType = uiCat.limitType
                )
            }
        }

        viewModelScope.launch {
            updateState { copy(isSaving = true) }
            saveBudgetUseCase.execute(currentYear, currentMonth, income, selectedPeriod, limitsData)
                .onSuccess {
                    updateState { copy(isSaving = false, isSavedSuccess = true) }
                    sendEffect(BudgetEditEffect.NavigateBack)
                }
                .onFailure { e ->
                    updateState { copy(isSaving = false, error = e.message) }
                }
        }
    }

    private fun deleteBudget() {
        viewModelScope.launch {
            updateState { copy(isSaving = true) }
            deleteBudgetUseCase.execute(currentYear, currentMonth)
                .onSuccess {
                    updateState { copy(isSaving = false, isSavedSuccess = true) }
                    sendEffect(BudgetEditEffect.NavigateBack)
                }
                .onFailure { e ->
                    updateState { copy(isSaving = false, error = "Ошибка удаления: ${e.message}") }
                }
        }
    }

    private fun String.parseToDouble() = this.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    private fun String.parseToDoubleOrNull() = this.replace(" ", "").replace(",", ".").toDoubleOrNull()
    private fun String.isValidInput() = this.all { it.isDigit() || it == '.' || it == ',' }

    private fun formatValue(value: Double): String {
        return if (value % 1.0 == 0.0) value.toInt().toString()
        else String.format("%.2f", value).replace('.', ',')
    }
}