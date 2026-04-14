package com.example.smartbudget.feature.category_search

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface CategorySearchEffect : UiEffect {
    data class NavigateBackWithResult(val categoryName: String) : CategorySearchEffect
    data object Exit : CategorySearchEffect
}