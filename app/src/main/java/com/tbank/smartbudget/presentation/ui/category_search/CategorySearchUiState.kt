package com.tbank.smartbudget.presentation.ui.category_search

import com.tbank.smartbudget.presentation.ui.category_search.SearchCategoryItem

data class CategorySearchUiState(
    val searchQuery: String = "",
    val searchResults: List<SearchCategoryItem> = emptyList()
)