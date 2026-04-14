package com.example.smartbudget.feature.category_search

import com.tbank.smartbudget.core.ui.common.UiState

data class CategorySearchUiState(
    val searchQuery: String = "",
    val searchResults: List<SearchCategoryItem> = emptyList(),
    val isLoading: Boolean = false
) : UiState