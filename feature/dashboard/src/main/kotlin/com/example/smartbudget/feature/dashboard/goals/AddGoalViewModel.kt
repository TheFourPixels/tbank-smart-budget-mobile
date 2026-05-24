package com.example.smartbudget.feature.dashboard.goals

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : BaseViewModel<AddGoalUiState, AddGoalIntent, AddGoalEffect>(
    AddGoalUiState()
) {

    override fun onIntent(intent: AddGoalIntent) {
        when (intent) {
            is AddGoalIntent.OnNameChanged -> updateState { copy(name = intent.name) }
            is AddGoalIntent.OnAmountChanged -> {
                updateState { copy(amount = intent.amount) }
                calculateRecommendation()
            }
            is AddGoalIntent.OnDeadlineChanged -> {
                updateState { copy(deadline = intent.date) }
                calculateRecommendation()
            }
            AddGoalIntent.OnSaveClicked -> saveGoal()
            AddGoalIntent.OnBackClicked -> sendEffect(AddGoalEffect.NavigateBack)
        }
    }

    private fun calculateRecommendation() {
        val targetAmount = currentState.amount.toDoubleOrNull() ?: 0.0
        val now = LocalDate.now()
        val deadline = currentState.deadline
        
        val months = Period.between(now, deadline).let { 
            it.years * 12 + it.months 
        }.coerceAtLeast(1)
        
        val recommended = if (targetAmount > 0) targetAmount / months else 0.0
        updateState { copy(recommendedMonthly = recommended) }
    }

    private fun saveGoal() {
        if (currentState.name.isBlank()) {
            updateState { copy(error = "Введите название цели") }
            return
        }
        val targetAmount = currentState.amount.toDoubleOrNull() ?: 0.0
        if (targetAmount <= 0) {
            updateState { copy(error = "Введите корректную сумму") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isSaving = true) }
            goalRepository.createGoal(
                name = currentState.name,
                targetAmount = targetAmount,
                deadline = currentState.deadline.format(DateTimeFormatter.ISO_LOCAL_DATE)
            ).onSuccess {
                sendEffect(AddGoalEffect.NavigateBack)
            }.onFailure { error ->
                updateState { copy(isSaving = false, error = "Ошибка сохранения: ${error.message}") }
            }
        }
    }
}
