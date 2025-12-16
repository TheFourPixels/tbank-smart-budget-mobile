package com.tbank.smartbudget.presentation.ui.category_search

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.presentation.ui.category_search.SearchCategoryItem
import com.tbank.smartbudget.domain.usecase.GetCategoriesForSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySearchViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesForSearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategorySearchUiState())
    val uiState: StateFlow<CategorySearchUiState> = _uiState.asStateFlow()

    // Храним полный список, чтобы фильтровать локально без повторных запросов
    private var allCategories: List<BudgetCategory> = emptyList()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            // Используем UseCase для получения данных
            allCategories = getCategoriesUseCase()
            // Показываем полный список при старте
            updateSearchResults("")
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        updateSearchResults(query)
    }

    private fun updateSearchResults(query: String) {
        val trimmedQuery = query.trim()

        val filteredItems = if (trimmedQuery.isEmpty()) {
            // Если запрос пуст, показываем все, TopResult не нужен
            allCategories.map { mapToUiModel(it, isTop = false) }
        } else {
            // 1. Фильтруем по вхождению
            val matches = allCategories.filter {
                it.name.contains(trimmedQuery, ignoreCase = true)
            }

            // 2. Ищем идеальное совпадение для "Top Result" (начало строки)
            val topResultMatch = matches.firstOrNull {
                it.name.startsWith(trimmedQuery, ignoreCase = true)
            }

            // 3. Формируем список
            matches.map { category ->
                // Категория является TopResult, если она совпала с topResultMatch
                val isTop = category == topResultMatch
                mapToUiModel(category, isTop)
            }.sortedByDescending { it.isTopResult } // Поднимаем TopResult наверх списка
        }

        _uiState.update { it.copy(searchResults = filteredItems) }
    }

    private fun mapToUiModel(domainCategory: BudgetCategory, isTop: Boolean): SearchCategoryItem {

        return SearchCategoryItem(
            id = domainCategory.id,
            name = domainCategory.name,
            iconRes = domainCategory.iconRes,
            color = Color(domainCategory.color), // Конвертируем Long -> Color
            limit = "Лимит: ...", // TODO: Добавить получение текущего лимита, если нужно
            isTopResult = isTop
        )
    }
}