package com.example.smartbudget.feature.operations

import androidx.lifecycle.viewModelScope
import com.example.smartbudget.feature.operations.AddTransactionEffect
import com.example.smartbudget.feature.operations.AddTransactionIntent
import com.example.smartbudget.feature.operations.AddTransactionUiState
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.repository.TransactionRepository
import com.tbank.smartbudget.data.domain.usecase.GetCategoriesForTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val getCategoriesUseCase: GetCategoriesForTransactionUseCase
) : BaseViewModel<AddTransactionUiState, AddTransactionIntent, AddTransactionEffect>(
    AddTransactionUiState()
) {

    init {
        onIntent(AddTransactionIntent.LoadCategories)
    }

    override fun onIntent(intent: AddTransactionIntent) {
        when (intent) {
            AddTransactionIntent.LoadCategories -> loadCategories()
            is AddTransactionIntent.OnAmountChanged -> updateState { copy(amount = intent.amount) }
            is AddTransactionIntent.OnTypeChanged -> updateState { copy(type = intent.type) }
            is AddTransactionIntent.OnCategorySelected -> updateState { copy(selectedCategoryId = intent.categoryId) }
            is AddTransactionIntent.OnDateChanged -> updateState { copy(date = intent.date) }
            is AddTransactionIntent.OnMerchantNameChanged -> updateState { copy(merchantName = intent.name) }
            AddTransactionIntent.OnSaveClicked -> saveTransaction()
            AddTransactionIntent.OnBackClicked -> sendEffect(AddTransactionEffect.NavigateBack)
            AddTransactionIntent.OnAddCategoryClicked -> sendEffect(AddTransactionEffect.NavigateToSelectedCategories)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getCategoriesUseCase(LocalDate.now().year, LocalDate.now().monthValue)
                .onSuccess { categories ->
                    updateState {
                        copy(
                            isLoading = false,
                            categories = categories,
                            selectedCategoryId = categories.firstOrNull { it.limitValue > 0 }?.categoryId?.value
                                ?: categories.firstOrNull()?.categoryId?.value
                        )
                    }
                }
                .onFailure {
                    updateState { copy(isLoading = false, error = "Не удалось загрузить категории") }
                }
        }
    }

    private fun saveTransaction() {
        val amount = currentState.amount.toDoubleOrNull() ?: 0.0
        val categoryId = currentState.selectedCategoryId ?: return

        viewModelScope.launch {
            updateState { copy(isSaving = true) }
            transactionRepository.createTransaction(
                amount = amount,
                type = currentState.type.name,
                categoryId = categoryId,
                date = currentState.date,
                description = "Добавлена вручную",
                merchantName = currentState.merchantName
            ).onSuccess {
                sendEffect(AddTransactionEffect.ShowToast("Транзакция добавлена"))
                sendEffect(AddTransactionEffect.NavigateBack)
            }.onFailure {
                updateState { copy(isSaving = false, error = "Ошибка сохранения") }
            }
        }
    }
}
