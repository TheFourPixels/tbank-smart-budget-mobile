package com.tbank.smartbudget.presentation.ui.selected_categories

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SelectedCategoriesViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SelectedCategoriesUiState())
    val uiState: StateFlow<SelectedCategoriesUiState> = _uiState.asStateFlow()

    // Исходный список всех доступных категорий (заглушка)
    private val allAvailableMock = listOf(
        SelectedCategoryUi(1, "Продукты", "15 операций", Color(0xFF42A5F5)),
        SelectedCategoryUi(2, "Маркетплейсы", "1 операция", Color(0xFF66BB6A)),
        SelectedCategoryUi(3, "Транспорт", "Лимит: 5 000 ₽", Color(0xFFFFA726)),
        SelectedCategoryUi(4, "Кафе", "Лимит: 10 000 ₽", Color(0xFFEF5350)),
        SelectedCategoryUi(5, "Спорт", "Без лимита", Color(0xFFAB47BC)),
    )

    init {
        // Инициализация:
        // Первые две категории - выбранные (как на скриншоте)
        // Остальные - доступные
        _uiState.update {
            it.copy(
                selectedCategories = allAvailableMock.take(2),
                availableCategories = allAvailableMock.drop(2)
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            val filtered = if (query.isBlank()) {
                allAvailableMock.drop(2) // Возвращаем исходный список доступных
            } else {
                allAvailableMock.drop(2).filter {
                    it.name.contains(query, ignoreCase = true)
                }
            }
            currentState.copy(searchQuery = query, availableCategories = filtered)
        }
    }

    fun onAddCategoryClick() {
        // Логика перехода на создание категории или открытие диалога
    }
}