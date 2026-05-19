package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import javax.inject.Inject

class GenerateCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategorySearchRepository
) {
    suspend fun execute(): Result<Unit> {
        return try {
            val existingCategories = categoryRepository.getAllCategories().map { it.name.lowercase() }.toSet()
            
            val newCategories = listOf(
                "Кино", "Театры", "Зоотовары", "Спорт", "Хобби", 
                "Книги", "Техника", "Дача", "Подарки", "Благотворительность"
            ).filter { it.lowercase() !in existingCategories }

            if (newCategories.isEmpty()) {
                return Result.success(Unit)
            }

            // Создаем до 5 новых категорий за раз
            newCategories.shuffled().take(5).forEach { name ->
                categoryRepository.createCategory(name)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
