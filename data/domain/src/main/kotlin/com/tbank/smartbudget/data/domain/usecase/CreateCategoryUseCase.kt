package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Сценарий: Создание новой пользовательской категории.
 * Logic: Проверка имени и передача данных для сохранения.
 */
class CreateCategoryUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend fun execute(name: String, iconRes: Int, color: Long): Result<BudgetCategory> {

        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Имя категории не может быть пустым."))
        }

        return repository.createCustomCategory(name, iconRes, color)
    }
}