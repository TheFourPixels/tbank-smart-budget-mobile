package com.example.smartbudget.feature.operations

import com.tbank.smartbudget.core.ui.common.UiEffect
import com.tbank.smartbudget.core.ui.common.UiIntent
import com.tbank.smartbudget.data.domain.model.TransactionType
import java.time.LocalDateTime

sealed class AddTransactionIntent : UiIntent {
    data object LoadCategories : AddTransactionIntent()
    data class OnAmountChanged(val amount: String) : AddTransactionIntent()
    data class OnTypeChanged(val type: TransactionType) : AddTransactionIntent()
    data class OnCategorySelected(val categoryId: Long) : AddTransactionIntent()
    data class OnDateChanged(val date: LocalDateTime) : AddTransactionIntent()
    data class OnMerchantNameChanged(val name: String) : AddTransactionIntent()
    data object OnSaveClicked : AddTransactionIntent()
    data object OnBackClicked : AddTransactionIntent()
    data object OnAddCategoryClicked : AddTransactionIntent()
}

sealed class AddTransactionEffect : UiEffect {
    data object NavigateBack : AddTransactionEffect()
    data object NavigateToSelectedCategories : AddTransactionEffect()
    data class ShowToast(val message: String) : AddTransactionEffect()
}
