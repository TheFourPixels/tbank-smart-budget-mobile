package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.domain.model.Transaction
import com.tbank.smartbudget.domain.model.TransactionType
import com.tbank.smartbudget.domain.repository.TransactionRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject

class MockTransactionRepositoryImpl @Inject constructor() : TransactionRepository {

    // Данные для теста (даты специально подобраны под октябрь 2025)
    private val mockTransactions = listOf(
        Transaction(1, 4500.25, TransactionType.EXPENSE, LocalDateTime.parse("2025-10-28T03:38:00"), "Еженедельная закупка продуктов", "Супермаркет 'Лента'", "Продукты", 0xFF43A047, 7),
        Transaction(2, 95000.0, TransactionType.INCOME, LocalDateTime.parse("2025-10-27T16:23:00"), "Зарплата за текущий месяц", "Работодатель ООО", "Зарплата", 0xFF1E88E5, 999),
        Transaction(4, 3200.0, TransactionType.EXPENSE, LocalDateTime.parse("2025-10-24T16:03:00"), "Оплата электроэнергии", "МосЭнергоСбыт", "Коммунальные услуги", 0xFFFBC02D, 8),
        Transaction(7, 5000.0, TransactionType.EXPENSE, LocalDateTime.parse("2025-10-18T02:55:00"), "Перевод долга", "Перевод Другу", "Не распределено", 0xFF9E9E9E, 999),
        Transaction(5, 2100.0, TransactionType.EXPENSE, LocalDateTime.parse("2025-10-17T12:26:00"), "Бензин АИ-95", "АЗС Лукойл", "Проезд", 0xFFE53935, 4),
        Transaction(6, 7999.0, TransactionType.EXPENSE, LocalDateTime.parse("2025-10-14T00:10:00"), "Покупка рубашки", "Zara Store", "Одежда", 0xFF8E24AA, 3),
        Transaction(3, 1250.0, TransactionType.EXPENSE, LocalDateTime.parse("2025-10-12T03:23:00"), "Ужин с друзьями", "Ресторан 'Уют'", "Рестораны", 0xFFFF7043, 10),
        Transaction(8, 150.0, TransactionType.EXPENSE, LocalDateTime.parse("2025-10-10T10:00:00"), "Кофе", "Кофейня", "Рестораны", 0xFFFF7043, 10),
        Transaction(9, 650.0, TransactionType.EXPENSE, LocalDateTime.parse("2025-10-10T11:00:00"), "Такси", "Яндекс Go", "Проезд", 0xFFE53935, 4),

        // Добавим транзакции в другой месяц для проверки фильтра (Ноябрь)
        Transaction(10, 1000.0, TransactionType.EXPENSE, LocalDateTime.parse("2025-11-01T10:00:00"), "Интернет", "Ростелеком", "Связь", 0xFF039BE5, 2)
    )

    override suspend fun getTransactions(
        page: Int,
        size: Int,
        categoryId: Long?,
        query: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Result<List<Transaction>> {
        delay(500) // Имитация сети

        var filtered = mockTransactions

        // 1. Фильтрация по строке поиска
        if (!query.isNullOrEmpty()) {
            filtered = filtered.filter {
                (it.merchantName?.contains(query, true) == true) ||
                        (it.description?.contains(query, true) == true)
            }
        }

        // 2. Фильтрация по дате (НОВАЯ ЛОГИКА)
        if (startDate != null) {
            filtered = filtered.filter { !it.date.isBefore(startDate) }
        }
        if (endDate != null) {
            // endDate обычно указывает на начало следующего дня или конец текущего,
            // для надежности добавим 1 день к endDate и проверим isBefore,
            // либо убедимся что время endDate 23:59:59.
            // Здесь предполагаем, что endDate передан как конец дня.
            val endOfDay = endDate.withHour(23).withMinute(59).withSecond(59)
            filtered = filtered.filter { !it.date.isAfter(endOfDay) }
        }

        // 3. Пагинация
        val fromIndex = page * size
        if (fromIndex >= filtered.size) return Result.success(emptyList())
        val toIndex = minOf(fromIndex + size, filtered.size)

        return Result.success(filtered.subList(fromIndex, toIndex))
    }

    override suspend fun createTransaction(
        amount: Double,
        type: String,
        categoryId: Long,
        date: LocalDateTime,
        description: String?,
        merchantName: String?
    ): Result<Transaction> {
        delay(500)
        return Result.success(
            Transaction((100..10000).random().toLong(), amount, TransactionType.valueOf(type), date, description, merchantName, "New", 0xFFCCCCCC, categoryId)
        )
    }
}