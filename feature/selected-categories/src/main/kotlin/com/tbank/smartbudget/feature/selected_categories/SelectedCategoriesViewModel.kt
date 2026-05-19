package com.tbank.smartbudget.feature.selected_categories

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.model.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.model.BudgetLimitModel
import com.tbank.smartbudget.data.domain.model.BudgetLimitType
import com.tbank.smartbudget.data.domain.model.BudgetLimitData
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import com.tbank.smartbudget.data.domain.usecase.GetBudgetDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SelectedCategoriesViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val categorySearchRepository: CategorySearchRepository,
    private val getBudgetDetailsUseCase: GetBudgetDetailsUseCase
) : BaseViewModel<SelectedCategoriesUiState, SelectedCategoriesIntent, SelectedCategoriesEffect>(
    SelectedCategoriesUiState()
) {

    private var allSelected: List<SelectedCategoryUi> = emptyList()
    private var allAvailable: List<SelectedCategoryUi> = emptyList()

    private val currentYear get() = LocalDate.now().year
    private val currentMonth get() = LocalDate.now().monthValue

    init {
        onIntent(SelectedCategoriesIntent.LoadData)
    }

    override fun onIntent(intent: SelectedCategoriesIntent) {
        when (intent) {
            SelectedCategoriesIntent.LoadData -> loadData()
            is SelectedCategoriesIntent.OnSearchQueryChanged -> handleSearch(intent.query)
            is SelectedCategoriesIntent.OnCategorySelected -> selectCategory(intent.category)
            is SelectedCategoriesIntent.OnCategoryRemoved -> removeCategory(intent.category)
            
            SelectedCategoriesIntent.OnCreateCategoryClick -> updateState { 
                copy(creationStep = CategoryCreationStep.NAME, newCategoryName = "", newCategoryLimit = "") 
            }
            SelectedCategoriesIntent.OnDismissDialog -> updateState { copy(creationStep = CategoryCreationStep.HIDDEN) }
            is SelectedCategoriesIntent.OnNewCategoryNameChanged -> updateState { copy(newCategoryName = intent.name) }
            SelectedCategoriesIntent.OnNameStepSubmit -> updateState { copy(creationStep = CategoryCreationStep.LIMIT) }
            is SelectedCategoriesIntent.OnNewCategoryLimitChanged -> {
                if (intent.limit.all { it.isDigit() || it == '.' || it == ',' }) {
                    updateState { copy(newCategoryLimit = intent.limit) }
                }
            }
            SelectedCategoriesIntent.OnLimitStepSubmit -> handleFullCreation()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            // Используем UseCase для получения обогащенных данных (с именами)
            val budgetResult = getBudgetDetailsUseCase(currentYear, currentMonth)
            val categoriesResult = categorySearchRepository.getAllCategories()

            if (budgetResult.isSuccess) {
                val limits = budgetResult.getOrNull()?.limits ?: emptyList()
                val allCats = categoriesResult

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
            updateState { copy(isLoading = true) }
            budgetRepository.addCategoryToBudget(currentYear, currentMonth, category.id.value)
                .onSuccess { loadData() }
                .onFailure { updateState { copy(isLoading = false, error = "Не удалось добавить категорию") } }
        }
    }

    private fun removeCategory(category: SelectedCategoryUi) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            budgetRepository.removeCategoryFromBudget(currentYear, currentMonth, category.id.value)
                .onSuccess { loadData() }
                .onFailure { updateState { copy(isLoading = false, error = "Не удалось удалить категорию") } }
        }
    }

    private fun handleFullCreation() {
        val name = currentState.newCategoryName
        val limitStr = currentState.newCategoryLimit
        val limitValue = limitStr.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0

        viewModelScope.launch {
            updateState { copy(isLoading = true, creationStep = CategoryCreationStep.HIDDEN) }
            
            // 1. Создаем категорию
            categorySearchRepository.createCategory(name)
                .onSuccess { categoryId ->
                    // 2. Добавляем в бюджет
                    budgetRepository.addCategoryToBudget(currentYear, currentMonth, categoryId)
                    
                    // 3. Обновляем лимит всего бюджета
                    val currentBudget = budgetRepository.getBudgetDetails(currentYear, currentMonth).getOrNull()
                    if (currentBudget != null) {
                        val newLimits = currentBudget.limits.map { 
                            BudgetLimitData(it.categoryId.value, it.limitValue, it.limitType) 
                        }.filter { it.categoryId != categoryId } + BudgetLimitData(categoryId, limitValue, BudgetLimitType.SUM)
                        
                        budgetRepository.saveBudget(
                            year = currentYear,
                            month = currentMonth,
                            totalIncome = currentBudget.totalIncome,
                            period = currentBudget.period,
                            limits = newLimits
                        )
                    }
                    
                    loadData()
                }
                .onFailure {
                    updateState { copy(isLoading = false, error = "Не удалось создать категорию") }
                }
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

    private fun BudgetLimitModel.toUi(): SelectedCategoryUi {
        val limitDesc = if (limitType == BudgetLimitType.PERCENT)
            "Лимит: ${limitValue.toInt()}%"
        else "Лимит: ${formatMoney(limitValue)}"

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
    
    private fun formatMoney(amount: Double): String {
        return "%,.0f ₽".format(amount).replace(',', ' ')
    }
}
