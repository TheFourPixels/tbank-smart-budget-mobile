package com.tbank.smartbudget.data.repository.mappers

import com.tbank.smartbudget.core.network.remote.dto.*
import com.tbank.smartbudget.data.domain.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

/**
 * Маппинг DTO транзакции в доменную модель.
 */
fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = TransactionId(id),
        amount = amount,
        type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
        date = try {
            ZonedDateTime.parse(date).toLocalDateTime()
        } catch (e: Exception) {
            LocalDateTime.parse(date)
        },
        description = description,
        merchantName = merchant,
        categoryName = categoryName ?: "Без категории",
        categoryColor = categoryColor ?: 0L,
        categoryId = CategoryId(categoryId ?: 0L)
    )
}

/**
 * Маппинг DTO лимита в доменную модель.
 * Поля name, iconRes и color остаются дефолтными, так как они
 * обогащаются в GetBudgetDetailsUseCase из CategorySearchRepository.
 */
fun BudgetLimitDto.toDomain(): BudgetLimitModel {
    return BudgetLimitModel(
        categoryId = CategoryId(categoryId),
        categoryName = "", // Будет заполнено в UseCase
        limitValue = limitValue,
        limitType = if (limitType == "PERCENT") BudgetLimitType.PERCENT else BudgetLimitType.AMOUNT,
        iconRes = 0,
        color = 0L
    )
}

/**
 * Маппинг DTO цели в доменную модель.
 */
fun GoalDto.toDomain(): Goal {
    val progress = if (targetAmount > 0) {
        ((currentAmount / targetAmount) * 100).toInt().coerceIn(0, 100)
    } else 0
    return Goal(
        id = GoalId(id),
        name = name,
        targetAmount = targetAmount,
        savedAmount = currentAmount,
        deadline = deadline,
        progressPercent = progress
    )
}

/**
 * Маппинг DTO пользователя в доменную модель.
 */
fun UserDto.toDomain(): User {
    return User(
        id = UserId(id),
        name = name,
        email = email,
        avatarUrl = avatarUrl
    )
}

/**
 * Преобразование статистики категорий из DTO в доменную модель.
 */
fun DashboardCategoryStat.toDomain(): CategoryLimit {
    return CategoryLimit(
        id = CategoryId(categoryId),
        name = categoryName ?: "Категория",
        limitAmount = budgetLimit,
        spentAmount = spentAmount,
        iconRes = 0,
        color = 0L
    )
}

/**
 * Преобразование сводки из DTO в доменную модель.
 */
fun BudgetDashboardDto.toSummary(): BudgetSummary {
    val monthName = LocalDate.now().month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
        .replaceFirstChar { it.uppercase() }

    return BudgetSummary(
        totalIncome = totalIncome,
        totalLimit = totalSpent + remainingBudget,
        totalSpent = totalSpent,
        freeFunds = remainingBudget,
        period = monthName
    )
}


/**
 * Маппинг основного DTO бюджета.
 */
fun BudgetDto.toDomain(): BudgetDetails {
    return BudgetDetails(
        id = BudgetId(id),
        year = year,
        month = month,
        totalIncome = totalIncome,
        period = "Месяц",
        limits = limits.map { it.toDomain() }
    )
}