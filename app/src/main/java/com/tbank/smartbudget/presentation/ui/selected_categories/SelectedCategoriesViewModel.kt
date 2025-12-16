package com.tbank.smartbudget.presentation.ui.selected_categories

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.model.BudgetLimitModel
import com.tbank.smartbudget.domain.model.BudgetLimitType
import com.tbank.smartbudget.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedCategoriesViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectedCategoriesUiState())
    val uiState: StateFlow<SelectedCategoriesUiState> = _uiState.asStateFlow()

    private var allSelected: List<SelectedCategoryUi> = emptyList()
    private var allAvailable: List<SelectedCategoryUi> = emptyList()

    private val currentYear = 2025
    private val currentMonth = 12

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Загружаем данные из репозитория
            val budgetResult = repository.getBudgetDetails(currentYear, currentMonth)
            val categoriesResult = repository.getAllAvailableCategories()

            if (budgetResult.isSuccess && categoriesResult.isSuccess) {
                val limits = budgetResult.getOrNull()?.limits ?: emptyList()
                val allCats = categoriesResult.getOrNull() ?: emptyList()

                // 1. Формируем список "Выбранные"
                allSelected = limits.map { it.toUi() }

                // 2. Формируем список "Доступные" (все минус выбранные)
                val selectedIds = limits.map { it.categoryId }.toSet()
                allAvailable = allCats
                    .filter { it.id !in selectedIds }
                    .map { it.toUi() }

                updateUiState()
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        updateUiState()
    }

    /**
     * Выбор категории: Добавляем в репозиторий и обновляем UI
     */
    fun onCategorySelected(category: SelectedCategoryUi) {
        viewModelScope.launch {
            // Оптимистичное обновление UI
            val newSelected = category.copy(limitDescription = "Лимит не установлен")
            allAvailable = allAvailable.filter { it.id != category.id }
            allSelected = allSelected + newSelected
            updateUiState()

            // Сохранение в БД
            repository.addCategoryToBudget(currentYear, currentMonth, category.id)
        }
    }

    /**
     * Удаление категории: Удаляем из репозитория и обновляем UI
     */
    fun onCategoryRemoved(category: SelectedCategoryUi) {
        viewModelScope.launch {
            // Оптимистичное обновление UI
            val newAvailable = category.copy(limitDescription = "")
            allSelected = allSelected.filter { it.id != category.id }
            allAvailable = (allAvailable + newAvailable).sortedBy { it.id }
            updateUiState()

            // Удаление из БД
            repository.removeCategoryFromBudget(currentYear, currentMonth, category.id)
        }
    }

    fun onAddCategoryClick() {
        // Навигация на создание новой категории (пока не реализовано)
    }

    private fun updateUiState() {
        val query = _uiState.value.searchQuery.trim()
        val filteredAvailable = if (query.isEmpty()) {
            allAvailable
        } else {
            allAvailable.filter { it.name.contains(query, ignoreCase = true) }
        }

        _uiState.update {
            it.copy(
                selectedCategories = allSelected,
                availableCategories = filteredAvailable
            )
        }
    }

    // --- Mappers ---
    private fun BudgetLimitModel.toUi(): SelectedCategoryUi {
        val limitDesc = if (limitType == BudgetLimitType.PERCENT)
            "Лимит: ${limitValue.toInt()}%"
        else "Лимит: ${limitValue.toInt()} ₽"

        return SelectedCategoryUi(categoryId, categoryName, limitDesc, Color(color), iconRes)
    }

    private fun BudgetCategory.toUi() = SelectedCategoryUi(id, name, "", Color(color), iconRes)
}