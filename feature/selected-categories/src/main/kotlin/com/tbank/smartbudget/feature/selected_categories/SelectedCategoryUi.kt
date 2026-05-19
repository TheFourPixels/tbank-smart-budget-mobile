package com.tbank.smartbudget.feature.selected_categories

import androidx.compose.ui.graphics.Color
import com.tbank.smartbudget.core.ui.common.UiState
import com.tbank.smartbudget.data.domain.model.CategoryId

data class SelectedCategoryUi(
    val id: CategoryId,
    val name: String,
    val limitDescription: String,
    val color: Color,
    val iconRes: Int = 0
)

enum class CategoryCreationStep {
    HIDDEN, NAME, LIMIT
}

data class SelectedCategoriesUiState(
    val searchQuery: String = "",
    val selectedCategories: List<SelectedCategoryUi> = emptyList(),
    val availableCategories: List<SelectedCategoryUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    
    // Поля для диалога создания
    val creationStep: CategoryCreationStep = CategoryCreationStep.HIDDEN,
    val newCategoryName: String = "",
    val newCategoryLimit: String = ""
) : UiState
