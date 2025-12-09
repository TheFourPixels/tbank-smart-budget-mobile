package com.tbank.smartbudget.domain.usecase

import com.tbank.smartbudget.domain.model.BudgetCategory
import com.tbank.smartbudget.domain.repository.CategorySearchRepository
import javax.inject.Inject

/**
 * UseCase для получения списка всех доступных категорий, которые пользователь может
 * выбрать или искать на экране поиска.
 *
 * @param repository Интерфейс репозитория для доступа к данным.
 */
class GetCategoriesForSearchUseCase @Inject constructor(
    private val repository: CategorySearchRepository
) {
    /**
     * Возвращает список всех доступных категорий.
     *
     * @return List<BudgetCategory> Список категорий.
     */
    suspend operator fun invoke(): List<BudgetCategory> {
        return repository.getAllCategories()
    }
}