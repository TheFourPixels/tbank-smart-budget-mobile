package com.tbank.smartbudget.feature.selected_categories

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.core.ui.common.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.model.BudgetLimitModel
import com.tbank.smartbudget.data.domain.model.BudgetLimitType
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedCategoriesViewModel @Inject constructor(
    private val repository: BudgetRepository
) : BaseViewModel<SelectedCategoriesUiState, SelectedCategoriesIntent, SelectedCategoriesEffect>(
    SelectedCategoriesUiState()
) {

    private var allSelected: List<SelectedCategoryUi> = emptyList()
    private var allAvailable: List<SelectedCategoryUi> = emptyList()

    private val currentYear = 2025
    private val currentMonth = 12

    init {
        onIntent(SelectedCategoriesIntent.LoadData)
    }

    override fun onIntent(intent: SelectedCategoriesIntent) {
        when (intent) {
            SelectedCategoriesIntent.LoadData -> loadData()
            is SelectedCategoriesIntent.OnSearchQueryChanged -> handleSearch(intent.query)
            is SelectedCategoriesIntent.OnCategorySelected -> selectCategory(intent.category)
            is SelectedCategoriesIntent.OnCategoryRemoved -> removeCategory(intent.category)
            SelectedCategoriesIntent.OnCreateCategoryClick -> sendEffect(SelectedCategoriesEffect.NavigateToCreateCategory)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val budgetResult = repository.getBudgetDetails(currentYear, currentMonth)
            val categoriesResult = repository.getAllAvailableCategories()

            if (budgetResult.isSuccess && categoriesResult.isSuccess) {
                val limits = budgetResult.getOrNull()?.limits ?: emptyList()
                val allCats = categoriesResult.getOrNull() ?: emptyList()

                allSelected = limits.map { it.toUi() }
                val selectedIds = limits.map { it.categoryId }.toSet()

                allAvailable = allCats
                    .filter { it.id !in selectedIds }
                    .map { it.toUi() }

                syncAndFilteredState()
            } else {
                updateState { copy(isLoading = false, error = "Ошибка загрузки данных") }
            }
        }
    }

    private fun handleSearch(query: String) {
        updateState { copy(searchQuery = query) }
        syncAndFilteredState()
    }

    private fun selectCategory(category: SelectedCategoryUi) {
        viewModelScope.launch {
            val newSelected = category.copy(limitDescription = "Лимит не установлен")
            allAvailable = allAvailable.filter { it.id != category.id }
            allSelected = allSelected + newSelected
            syncAndFilteredState()

            repository.addCategoryToBudget(currentYear, currentMonth, category.id.value)
        }
    }

    private fun removeCategory(category: SelectedCategoryUi) {
        viewModelScope.launch {
            val newAvailable = category.copy(limitDescription = "")
            allSelected = allSelected.filter { it.id != category.id }
            allAvailable = (allAvailable + newAvailable).sortedBy { it.id.value }
            syncAndFilteredState()

            repository.removeCategoryFromBudget(currentYear, currentMonth, category.id.value)
        }
    }

    private fun syncAndFilteredState() {
        val query = currentState.searchQuery.trim()
        val filteredAvailable = if (query.isEmpty()) {
            allAvailable
        } else {
            allAvailable.filter { it.name.contains(query, ignoreCase = true) }
        }

        updateState {
            copy(
                isLoading = false,
                selectedCategories = allSelected,
                availableCategories = filteredAvailable
            )
        }
    }

    // --- Mappers с использованием CategoryColorMapper ---

    private fun BudgetLimitModel.toUi(): SelectedCategoryUi {
        val limitDesc = if (limitType == BudgetLimitType.PERCENT)
            "Лимит: ${limitValue.toInt()}%"
        else "Лимит: ${limitValue.toInt()} ₽"

        return SelectedCategoryUi(
            id = categoryId,
            name = categoryName,
            limitDescription = limitDesc,
            color = Color(CategoryColorMapper.getColorForId(categoryId.value)),
            iconRes = iconRes
        )
    }

    private fun BudgetCategory.toUi() = SelectedCategoryUi(
        id = id,
        name = name,
        limitDescription = "",
        color = Color(CategoryColorMapper.getColorForId(id.value)),
        iconRes = iconRes
    )
}