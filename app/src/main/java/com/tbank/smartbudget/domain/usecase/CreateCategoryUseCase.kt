package com.tbank.smartbudget.domain.usecase

import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Сценарий: Создание новой пользовательской категории.
 * Logic: Проверка имени и передача данных для сохранения.
 */
class CreateCategoryUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend fun execute(name: String, iconRes: Int, color: Long): Result<BudgetCategory> {

        // 1. Валидация: Имя категории
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Имя категории не может быть пустым."))
        }

        // 2. Сохранение в слое Data
        return repository.createCustomCategory(name, iconRes, color)
    }
}