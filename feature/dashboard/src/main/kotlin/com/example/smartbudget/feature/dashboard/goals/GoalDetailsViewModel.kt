package com.example.smartbudget.feature.dashboard.goals

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalDetailsViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : BaseViewModel<GoalDetailsUiState, GoalDetailsIntent, GoalDetailsEffect>(
    GoalDetailsUiState()
) {

    override fun onIntent(intent: GoalDetailsIntent) {
        when (intent) {
            is GoalDetailsIntent.LoadGoal -> loadGoal(intent.id)
            is GoalDetailsIntent.OnContributeClicked -> contribute(intent.amount)
            GoalDetailsIntent.OnCompleteEarlyClicked -> completeEarly()
            GoalDetailsIntent.OnBackClicked -> sendEffect(GoalDetailsEffect.NavigateBack)
        }
    }

    private fun loadGoal(id: Long) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            goalRepository.getGoalDetails(id)
                .onSuccess { goal ->
                    updateState { copy(isLoading = false, goal = goal, error = null) }
                }
                .onFailure { error ->
                    updateState { copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun contribute(amount: Double) {
        val goalId = currentState.goal?.id?.value ?: return
        viewModelScope.launch {
            updateState { copy(isContributing = true) }
            goalRepository.contributeToGoal(goalId, amount)
                .onSuccess { updatedGoal ->
                    updateState { copy(isContributing = false, goal = updatedGoal) }
                }
                .onFailure { error ->
                    updateState { copy(isContributing = false, error = error.message) }
                }
        }
    }

    private fun completeEarly() {
        val goalId = currentState.goal?.id?.value ?: return
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            goalRepository.deleteGoal(goalId)
                .onSuccess {
                    sendEffect(GoalDetailsEffect.ShowToast("Цель завершена"))
                    sendEffect(GoalDetailsEffect.NavigateBack)
                }
                .onFailure { error ->
                    updateState { copy(isLoading = false, error = "Ошибка: ${error.message}") }
                }
        }
    }
}
