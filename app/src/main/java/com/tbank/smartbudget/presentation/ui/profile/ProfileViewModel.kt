package com.tbank.smartbudget.presentation.ui.profile

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Делаем запрос к API через репозиторий
            val result = authRepository.getProfile()

            // 2. Извлекаем имя. Если ошибка или null - ставим дефолтное
            val loadedUserName = result.getOrNull()?.name ?: "Пользователь"

            // 3. Моковые бюджеты оставляем для UI (пока нет реального API для них)
            val mockBudgets = listOf(
                BudgetProfileItem(
                    id = 1,
                    name = "Кубышка",
                    dateDescription = "Январь 2026",
                    color = Color(0xFFAD1457), // Малиновый
                    initial = "Я"
                ),
                BudgetProfileItem(
                    id = 2,
                    name = "Копилка",
                    dateDescription = "Февраль 2026",
                    color = Color(0xFFF9A825), // Оранжевый
                    initial = "Ф"
                ),
                BudgetProfileItem(
                    id = 3,
                    name = "Отпуск",
                    dateDescription = "Июль 2026",
                    color = Color(0xFF2E7D32), // Зеленый
                    initial = "О"
                )
            )

            // 4. Обновляем UI
            _uiState.update {
                it.copy(
                    isLoading = false,
                    userName = loadedUserName,
                    budgets = mockBudgets
                )
            }
        }
    }
}