package com.tbank.smartbudget.feature.selected_categories

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface SelectedCategoriesIntent : UiIntent {
    data object LoadData : SelectedCategoriesIntent
    data class OnSearchQueryChanged(val query: String) : SelectedCategoriesIntent
    data class OnCategorySelected(val category: SelectedCategoryUi) : SelectedCategoriesIntent
    data class OnCategoryRemoved(val category: SelectedCategoryUi) : SelectedCategoriesIntent
    data object OnCreateCategoryClick : SelectedCategoriesIntent
}
