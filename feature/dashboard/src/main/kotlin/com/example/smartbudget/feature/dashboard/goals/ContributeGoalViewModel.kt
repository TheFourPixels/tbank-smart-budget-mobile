package com.example.smartbudget.feature.dashboard.goals

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContributeGoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : BaseViewModel<ContributeGoalUiState, ContributeGoalIntent, ContributeGoalEffect>(
    ContributeGoalUiState()
) {

    override fun onIntent(intent: ContributeGoalIntent) {
        when (intent) {
            is ContributeGoalIntent.Init -> updateState { 
                copy(
                    goalId = intent.goalId, 
                    amount = intent.amount.toInt().toString(),
                    targetAmount = intent.targetAmount,
                    savedAmount = intent.savedAmount
                ) 
            }
            is ContributeGoalIntent.OnAmountChanged -> updateState { copy(amount = intent.amount) }
            ContributeGoalIntent.OnContributeClicked -> contribute()
            ContributeGoalIntent.OnBackClicked -> sendEffect(ContributeGoalEffect.NavigateBack)
        }
    }

    private fun contribute() {
        val amount = currentState.amount.toDoubleOrNull() ?: return
        if (amount <= 0) return

        viewModelScope.launch {
            updateState { copy(isSaving = true) }
            goalRepository.contributeToGoal(currentState.goalId, amount)
                .onSuccess {
                    sendEffect(ContributeGoalEffect.NavigateBack)
                }
                .onFailure { error ->
                    updateState { copy(isSaving = false, error = "Ошибка: ${error.message}") }
                }
        }
    }
}
