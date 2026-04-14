package com.tbank.smartbudget.feature.selected_categories

import com.tbank.smartbudget.core.ui.common.UiEffect

sealed interface SelectedCategoriesEffect : UiEffect {
    data object NavigateToCreateCategory : SelectedCategoriesEffect
    data class ShowError(val message: String) : SelectedCategoriesEffect
}