package com.example.smartbudget.feature.dashboard.goals

import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : BaseViewModel<GoalsUiState, GoalsIntent, GoalsEffect>(
    GoalsUiState()
) {

    init {
        onIntent(GoalsIntent.LoadGoals)
    }

    override fun onIntent(intent: GoalsIntent) {
        when (intent) {
            GoalsIntent.LoadGoals -> loadGoals()
            GoalsIntent.OnBackClick -> sendEffect(GoalsEffect.NavigateBack)
            GoalsIntent.OnAddGoalClick -> sendEffect(GoalsEffect.NavigateToAddGoal)
        }
    }

    private fun loadGoals() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            val activeResult = goalRepository.getGoals()
            val completedResult = goalRepository.getCompletedGoals()

            if (activeResult.isSuccess || completedResult.isSuccess) {
                updateState {
                    copy(
                        isLoading = false,
                        activeGoals = activeResult.getOrNull() ?: emptyList(),
                        completedGoals = completedResult.getOrNull() ?: emptyList(),
                        error = null
                    )
                }
            } else {
                updateState { 
                    copy(
                        isLoading = false, 
                        error = activeResult.exceptionOrNull()?.message ?: "Ошибка загрузки"
                    ) 
                }
            }
        }
    }
}
