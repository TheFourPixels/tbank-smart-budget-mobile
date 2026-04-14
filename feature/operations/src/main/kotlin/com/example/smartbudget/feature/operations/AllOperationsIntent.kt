package com.example.smartbudget.feature.operations

import com.tbank.smartbudget.core.ui.common.UiIntent

sealed interface AllOperationsIntent : UiIntent {
    data object LoadData : AllOperationsIntent
    data class OnSearchQueryChanged(val query: String) : AllOperationsIntent
    data class OnCategorySearchResult(val categoryName: String) : AllOperationsIntent
    data class OnPeriodChanged(val periodType: PeriodType) : AllOperationsIntent
    data class OnCustomDateRangeSelected(val startMillis: Long?, val endMillis: Long?) : AllOperationsIntent
    data class OnCategorySelected(val categoryName: String) : AllOperationsIntent
    data object OnBackClick : AllOperationsIntent
    data object OnSearchClick : AllOperationsIntent
}