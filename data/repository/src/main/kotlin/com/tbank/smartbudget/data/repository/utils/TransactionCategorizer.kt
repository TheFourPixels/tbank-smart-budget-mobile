package com.tbank.smartbudget.data.repository.utils

import com.tbank.smartbudget.core.network.remote.api.TransactionApi
import com.tbank.smartbudget.core.network.remote.dto.CategorizationRule
import com.tbank.smartbudget.data.domain.model.BudgetCategory
import com.tbank.smartbudget.data.domain.model.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.CategoryId
import com.tbank.smartbudget.data.domain.model.Transaction
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionCategorizer @Inject constructor(
    private val transactionApi: TransactionApi,
    private val categoryRepository: CategorySearchRepository,
) {

    private suspend fun getRules(): List<CategorizationRule> {
        return try {
            transactionApi.getRules()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getCategories(): List<BudgetCategory> {
        return try {
            categoryRepository.getAllCategories()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun categorize(transactions: List<Transaction>): List<Transaction> {
        val rules = getRules()
        val categories = getCategories()

        return transactions.map { transaction ->
            // 1. Если категория уже назначена (ID > 0), просто обогащаем метаданными
            if (transaction.categoryId.value > 0) {
                val category = categories.find { it.id.value == transaction.categoryId.value }
                category?.let {
                    return@map transaction.copy(
                        categoryName = it.name,
                        categoryColor = CategoryColorMapper.getColorForId(it.id.value)
                    )
                }
            }

            // 2. Применяем правила категоризации
            val rule = rules.find {
                (transaction.merchantName?.contains(it.keyword, ignoreCase = true) == true) ||
                (transaction.description?.contains(it.keyword, ignoreCase = true) == true)
            }

            if (rule != null) {
                val category = categories.find { it.id.value == rule.categoryId }
                return@map transaction.copy(
                    categoryId = CategoryId(rule.categoryId),
                    categoryName = category?.name ?: "Категория ${rule.categoryId}",
                    categoryColor = CategoryColorMapper.getColorForId(rule.categoryId)
                )
            }

            // 3. Запасной вариант: Поиск по точному совпадению имени категории в описании или мерчанте
            // Это поможет, если категория была передана строкой или выбрана вручную, но ID потерялся
            val matchedCategory = categories.find { 
                (it.name.equals(transaction.merchantName, ignoreCase = true)) ||
                (it.name.equals(transaction.description, ignoreCase = true)) ||
                ((transaction.description?.contains(it.name, ignoreCase = true) == true) && (it.name.length > 3))
            }

            if (matchedCategory != null) {
                return@map transaction.copy(
                    categoryId = matchedCategory.id,
                    categoryName = matchedCategory.name,
                    categoryColor = matchedCategory.color
                )
            }

            // Оставляем без изменений, если ничего не найдено
            transaction
        }
    }
}
