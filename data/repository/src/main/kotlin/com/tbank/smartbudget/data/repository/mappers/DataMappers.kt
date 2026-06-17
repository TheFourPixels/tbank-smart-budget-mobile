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
    val ldt = try {
        ZonedDateTime.parse(transactionDate).toLocalDateTime()
    } catch (e: Exception) {
        try {
            LocalDateTime.parse(transactionDate)
        } catch (e2: Exception) {
            LocalDateTime.now()
        }
    }
    val finalCategoryName = category?.name ?: "Без категории"
    val finalCategoryId = category?.id ?: 0L
    val color = if (finalCategoryId > 0) {
        CategoryColorMapper.getColorForId(finalCategoryId)
    } else {
        CategoryColorMapper.getColorForName(finalCategoryName)
    }

    return Transaction(
        id = TransactionId(id),
        amount = amount,
        type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
        date = ldt,
        description = description,
        merchantName = merchantName,
        categoryName = finalCategoryName,
        categoryColor = color,
        categoryId = CategoryId(finalCategoryId)
    )
}

/**
 * Маппинг RecentTransactionDto в доменную модель Transaction.
 */
fun RecentTransactionDto.toDomain(): Transaction {
    val dateStr = date
    val ldt = when {
        dateStr == null -> LocalDateTime.now()
        else -> try {
            ZonedDateTime.parse(dateStr).toLocalDateTime()
        } catch (e: Exception) {
            try {
                LocalDateTime.parse(dateStr)
            } catch (e2: Exception) {
                LocalDateTime.now()
            }
        }
    }
    val finalCategoryName = categoryName ?: "Без категории"
    return Transaction(
        id = TransactionId(0L), // Dashboard не возвращает ID
        amount = amount ?: 0.0,
        type = if (income) TransactionType.INCOME else TransactionType.EXPENSE,
        date = ldt,
        description = description,
        merchantName = merchant,
        categoryName = finalCategoryName,
        categoryColor = CategoryColorMapper.getColorForName(finalCategoryName),
        categoryId = CategoryId(0L)
    )
}

/**
 * Маппинг DTO лимита в доменную модель.
 */
fun BudgetLimitDto.toDomain(): BudgetLimitModel {
    return BudgetLimitModel(
        categoryId = CategoryId(categoryId),
        categoryName = "", // Будет заполнено в UseCase
        limitValue = limitValue,
        limitType = if (limitType == "PERCENT") BudgetLimitType.PERCENT else BudgetLimitType.SUM,
        iconRes = 0,
        color = 0L
    )
}

/**
 * Маппинг DTO цели в доменную модель.
 */
fun GoalDto.toDomain(): Goal {
    return Goal(
        id = GoalId(id),
        name = name,
        targetAmount = targetAmount,
        savedAmount = savedAmount,
        deadline = deadline,
        createdAt = createdAt,
        progressPercent = progressPercent,
        daysLeft = daysLeft,
        recommendedMonthly = recommendedMonthly,
        contributions = contributions?.map { it.toDomain() } ?: emptyList()
    )
}

fun GoalContributionDto.toDomain(): GoalContribution {
    val ldt = try {
        ZonedDateTime.parse(date).toLocalDateTime()
    } catch (e: Exception) {
        try {
            LocalDateTime.parse(date)
        } catch (e2: Exception) {
            LocalDateTime.now()
        }
    }
    return GoalContribution(
        amount = amount,
        date = ldt,
        description = description
    )
}

/**
 * Маппинг GoalSummaryDto в доменную модель Goal.
 */
fun GoalSummaryDto.toDomain(): Goal {
    return Goal(
        id = GoalId(id),
        name = name,
        targetAmount = target,
        savedAmount = saved,
        deadline = null,
        progressPercent = progressPercent,
        daysLeft = daysLeft,
        recommendedMonthly = recommendedMonthly
    )
}

/**
 * Маппинг DTO пользователя в доменную модель.
 */
fun UserProfileDto.toDomain(): User {
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
fun CategoryStatDto.toDomain(): CategoryLimit {
    val parsedColor = try {
        val colorStr = color
        if (colorStr != null && colorStr.startsWith("#")) {
            colorStr.removePrefix("#").toLong(16) or 0xFF000000
        } else {
            // Если пришло название цвета ("green", "red") или null - берем по ID
            CategoryColorMapper.getColorForId(categoryId)
        }
    } catch (e: Exception) {
        CategoryColorMapper.getColorForId(categoryId)
    }

    return CategoryLimit(
        id = CategoryId(categoryId),
        name = categoryName,
        limitAmount = limit,
        spentAmount = spent,
        iconRes = 0,
        color = parsedColor
    )
}

/**
 * Преобразование DashboardResponse в DashboardData.
 */
fun DashboardResponse.toDomain(): DashboardData {
    val finalIncome = if (totalIncome > 0) totalIncome else budgetPlan
    val finalStats = categoryStats ?: categoriesStats ?: emptyList()
    
    return DashboardData(
        month = month,
        totalIncome = finalIncome,
        totalSpent = totalSpent,
        remainingBudget = remainingBudget,
        spendingLimit = spendingLimit,
        categoryStats = finalStats.map { it.toDomain() },
        activeGoals = activeGoals?.map { it.toDomain() } ?: emptyList(),
        recentTransactions = recentTransactions?.map { it.toDomain() } ?: emptyList()
    )
}

/**
 * Преобразование DashboardResponse в BudgetSummary.
 */
fun DashboardResponse.toSummary(): BudgetSummary {
    val finalIncome = if (totalIncome > 0) totalIncome else budgetPlan
    val monthName = LocalDate.of(year, month, 1)
        .month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
        .replaceFirstChar { it.uppercase() }

    return BudgetSummary(
        totalIncome = finalIncome,
        totalLimit = totalSpent + remainingBudget,
        totalSpent = totalSpent,
        spendingLimit = spendingLimit,
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
