package com.example.smartbudget.feature.category_search

data class CategorySearchUiState(
    val searchQuery: String = "",
    val searchResults: List<SearchCategoryItem> = emptyList()
)