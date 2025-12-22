package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.repository.CategorySearchRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Мок-реализация репозитория для поиска категорий.
 * Имитирует получение чистых данных из базы, которые будут использоваться в UseCase и ViewModel.
 */
class MockCategorySearchRepositoryImpl @Inject constructor() : CategorySearchRepository {

    // Мок-данные для тестирования поиска
    private val MOCK_CATEGORIES = listOf(
        // Категории для тестирования поиска по "Транс"
        BudgetCategory(id = 1L, name = "Транспорт", iconRes = 0, color = 0xFFFBC02D), // Желтый
        BudgetCategory(id = 2L, name = "Транзакции", iconRes = 0, color = 0xFF43A047), // Зеленый

        // Категории для тестирования поиска по "Мар"
        BudgetCategory(id = 3L, name = "Маркетплейсы", iconRes = 0, color = 0xFF1E88E5), // Синий
        BudgetCategory(id = 4L, name = "Маркетинг", iconRes = 0, color = 0xFFE53935), // Красный

        // Категории для тестирования поиска по "С"
        BudgetCategory(id = 5L, name = "Спорт", iconRes = 0, color = 0xFF9E9E9E), // Серый
        BudgetCategory(id = 6L, name = "Связь", iconRes = 0, color = 0xFFFF7043), // Оранжевый

        // Общие
        BudgetCategory(id = 7L, name = "Продукты", iconRes = 0, color = 0xFF43A047),
        BudgetCategory(id = 8L, name = "Путешествия", iconRes = 0, color = 0xFF00BCD4),
        BudgetCategory(id = 9L, name = "Развлечения", iconRes = 0, color = 0xFF673AB7),
        BudgetCategory(id = 10L, name = "Здоровье", iconRes = 0, color = 0xFFFF7043),
    )

    override suspend fun getAllCategories(): List<BudgetCategory> {
        delay(300) // Имитация небольшой задержки сети
        return MOCK_CATEGORIES
    }
}