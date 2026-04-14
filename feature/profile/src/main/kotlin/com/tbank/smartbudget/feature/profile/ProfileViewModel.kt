package com.tbank.smartbudget.feature.profile

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.network.remote.AppResult
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.core.ui.common.CategoryColorMapper
import com.tbank.smartbudget.data.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<ProfileUiState, ProfileIntent, ProfileEffect>(
    ProfileUiState(isLoading = true)
) {

    init {
        onIntent(ProfileIntent.LoadProfile)
    }

    override fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile -> loadProfileData()
            is ProfileIntent.OnBudgetSelected -> {
                sendEffect(ProfileEffect.NavigateToBudgetDetails(intent.budgetId))
            }
        }
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val result = authRepository.getProfile()

            val loadedUserName = when (result) {
                is AppResult.Success -> result.data.name
                is AppResult.Error -> "Пользователь"
            }

            val mockBudgets = listOf(
                createMockBudget(1, "Кубышка", "Январь 2026", "Я"),
                createMockBudget(2, "Копилка", "Февраль 2026", "Ф"),
                createMockBudget(3, "Отпуск", "Июль 2026", "О")
            )

            updateState {
                copy(
                    isLoading = false,
                    userName = loadedUserName,
                    budgets = mockBudgets
                )
            }
        }
    }

    private fun createMockBudget(id: Long, name: String, date: String, initial: String): BudgetProfileItem {
        return BudgetProfileItem(
            id = id,
            name = name,
            dateDescription = date,
            initial = initial,
            color = Color(CategoryColorMapper.getColorForId(id))
        )
    }
}