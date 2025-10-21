package com.tbank.smartbudget.presentation.ui.setup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Модель для отображения категории на экране настроек
data class SetupCategoryUi(
    val id: Int,
    val name: String,
    val limit: String,
    val iconResId: Int = 0 // Заглушка для иконки
)

// Новая модель для отображения на главном экране (Вкладка Бюджет)
data class BudgetTabCategoryUi(
    val id: Int,
    val name: String,
    val operationsCount: String,
    val iconResId: Int = 0,
    val color: Long = 0xFF42A5F5 // Синий по умолчанию
)

data class BudgetSetupState(
    // Настройки
    val selectedMonths: Int = 3,
    val contributionAmount: String = "13 900",
    val selectedCurrency: String = "Рубли",
    val sourceCardLast4: String = "8563",
    val sourceCardName: String = "Дебетовая карта",
    val sourceCardBalance: String = "36 000 ₽",
    val setupCategories: List<SetupCategoryUi> = emptyList(), // Список для экрана настроек

    // Главный экран (Вкладка Бюджет)
    val userName: String = "Валерия",
    val budgetName: String = "Кубышка",
    val budgetBalance: String = "13 900 ₽",
    val budgetTerm: String = "2 месяца",
    val totalSpent: String = "5 429 ₽",
    val totalSpentDescription: String = "Трат в октябре",
    val selectedCategories: List<BudgetTabCategoryUi> = listOf( // Список для главного экрана
        BudgetTabCategoryUi(1, "Продукты", "15 операций за месяц"),
        BudgetTabCategoryUi(2, "Маркетплейсы", "1 операция за месяц", color = 0xFFFFA726) // Оранжевый
    )
)

@HiltViewModel
class BudgetSetupViewModel @Inject constructor(
    // Здесь должен быть UseCase для загрузки и сохранения настроек
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        BudgetSetupState(
            setupCategories = listOf(
                SetupCategoryUi(1, "Маркетплейсы", "2 300 ₽"),
                SetupCategoryUi(2, "Продукты", "14 600 ₽")
            )
        )
    )
    val uiState: StateFlow<BudgetSetupState> = _uiState

    fun onMonthsSelected(months: Int) {
        _uiState.update { it.copy(selectedMonths = months) }
    }

    fun onSaveClicked() {
        // Логика сохранения настроек
        println("Сохранение настроек...")
    }

    // Здесь должны быть функции для редактирования лимитов и добавления категорий
}
