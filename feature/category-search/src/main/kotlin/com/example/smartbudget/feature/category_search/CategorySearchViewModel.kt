package com.example.smartbudget.feature.category_search

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.model.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.usecase.GetCategoriesForSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySearchViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesForSearchUseCase
) : BaseViewModel<CategorySearchUiState, CategorySearchIntent, CategorySearchEffect>(
    CategorySearchUiState()
) {

    private var allCategories: List<BudgetCategory> = emptyList()

    init {
        onIntent(CategorySearchIntent.LoadCategories)
    }

    override fun onIntent(intent: CategorySearchIntent) {
        when (intent) {
            CategorySearchIntent.LoadCategories -> loadCategories()
            is CategorySearchIntent.OnQueryChanged -> handleQueryChanged(intent.query)
            is CategorySearchIntent.OnCategorySelected -> {
                sendEffect(CategorySearchEffect.NavigateBackWithResult(intent.categoryName))
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            allCategories = getCategoriesUseCase()
            updateSearchResults(currentState.searchQuery)
            updateState { copy(isLoading = false) }
        }
    }

    private fun handleQueryChanged(query: String) {
        updateState { copy(searchQuery = query) }
        updateSearchResults(query)
    }

    private fun updateSearchResults(query: String) {
        val trimmedQuery = query.trim()

        val filteredItems = if (trimmedQuery.isEmpty()) {
            allCategories.map { mapToUiModel(it, isTop = false) }
        } else {
            val matches = allCategories.filter {
                it.name.contains(trimmedQuery, ignoreCase = true)
            }

            val topResultMatch = matches.firstOrNull {
                it.name.startsWith(trimmedQuery, ignoreCase = true)
            }

            matches.map { category ->
                val isTop = category == topResultMatch
                mapToUiModel(category, isTop)
            }.sortedByDescending { it.isTopResult }
        }

        updateState { copy(searchResults = filteredItems) }
    }

    private fun mapToUiModel(domainCategory: BudgetCategory, isTop: Boolean): SearchCategoryItem {
        return SearchCategoryItem(
            id = domainCategory.id,
            name = domainCategory.name,
            iconRes = domainCategory.iconRes,
            color = Color(CategoryColorMapper.getColorForId(domainCategory.id.value)),
            limit = "Лимит: ...",
            isTopResult = isTop
        )
    }
}