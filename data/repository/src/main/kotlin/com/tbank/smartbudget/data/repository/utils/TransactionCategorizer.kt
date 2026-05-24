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
        
        android.util.Log.d("Categorizer", "Starting categorization for ${transactions.size} transactions. Rules: ${rules.size}, Categories: ${categories.size}")

        return transactions.map { transaction ->
            // 1. Если категория уже назначена (ID > 0), просто обогащаем метаданными
            if (transaction.categoryId.value > 0) {
                val category = categories.find { it.id.value == transaction.categoryId.value }
                category?.let {
                    android.util.Log.d("Categorizer", "Tx [${transaction.merchantName}]: Already has ID ${transaction.categoryId.value}, enriched with name ${it.name}")
                    return@map transaction.copy(
                        categoryName = it.name,
                        categoryColor = CategoryColorMapper.getColorForId(it.id.value)
                    )
                }
            }

            // 2. Применяем правила категоризации
            val rule = rules.find { rule ->
                val keyword = rule.keyword.lowercase().replace('ё', 'е')
                val merchant = transaction.merchantName?.lowercase()?.replace('ё', 'е') ?: ""
                val desc = transaction.description?.lowercase()?.replace('ё', 'е') ?: ""
                
                merchant.contains(keyword) || desc.contains(keyword)
            }

            if (rule != null) {
                val category = categories.find { it.id.value == rule.categoryId }
                android.util.Log.d("Categorizer", "Tx [${transaction.merchantName}]: Matched rule [${rule.keyword}] -> Category ${category?.name}")
                return@map transaction.copy(
                    categoryId = CategoryId(rule.categoryId),
                    categoryName = category?.name ?: "Категория ${rule.categoryId}",
                    categoryColor = CategoryColorMapper.getColorForId(rule.categoryId)
                )
            }

            // 3. Запасной вариант: Поиск по точному совпадению имени категории или базе знаний
            val matchedCategory = categories.find { category ->
                val catName = category.name.lowercase().replace('ё', 'е')
                val merchant = transaction.merchantName?.lowercase()?.replace('ё', 'е') ?: ""
                val desc = transaction.description?.lowercase()?.replace('ё', 'е') ?: ""
                
                // Встроенная база знаний для популярных сетей
                val isFood = merchant.contains("магнит") || merchant.contains("пятерочка") || merchant.contains("перекресток")
                val isTech = merchant.contains("dns") || merchant.contains("днс") || merchant.contains("мвидео")
                val isBook = merchant.contains("читай-город") || merchant.contains("лабиринт")
                val isClothes = merchant.contains("остин") || merchant.contains("gloria jeans") || merchant.contains("zara") || merchant.contains("hm")
                val isCafe = merchant.contains("кафе") || merchant.contains("ресторан") || merchant.contains("kfc") || merchant.contains("burger king")

                (catName == merchant || catName == desc || (desc.contains(catName) && catName.length > 3)) ||
                (catName == "продукты" && isFood) ||
                (catName == "техника" && isTech) ||
                (catName == "книги" && isBook) ||
                (catName == "одежда" && isClothes) ||
                (catName == "рестораны" && isCafe)
            }

            if (matchedCategory != null) {
                android.util.Log.d("Categorizer", "Tx [${transaction.merchantName}]: Matched category name [${matchedCategory.name}]")
                return@map transaction.copy(
                    categoryId = matchedCategory.id,
                    categoryName = matchedCategory.name,
                    categoryColor = matchedCategory.color
                )
            }

            android.util.Log.d("Categorizer", "Tx [${transaction.merchantName}]: No category found")
            // Оставляем без изменений, если ничего не найдено
            transaction
        }
    }
}
