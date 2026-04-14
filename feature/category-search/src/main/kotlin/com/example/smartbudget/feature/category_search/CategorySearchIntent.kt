package com.example.smartbudget.feature.category_search

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface CategorySearchIntent : UiIntent {
    data object LoadCategories : CategorySearchIntent
    data class OnQueryChanged(val query: String) : CategorySearchIntent
    data class OnCategorySelected(val categoryName: String) : CategorySearchIntent
}