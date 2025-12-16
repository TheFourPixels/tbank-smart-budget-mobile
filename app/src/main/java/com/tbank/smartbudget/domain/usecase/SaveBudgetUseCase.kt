package com.tbank.smartbudget.domain.usecase

import com.tbank.smartbudget.domain.model.BudgetLimitData
import com.tbank.smartbudget.domain.repository.BudgetRepository
import javax.inject.Inject
import kotlin.math.abs

/**
 * Сценарий: Создание или обновление бюджета с валидацией.
 */
class SaveBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    private val VALIDATION_TOLERANCE = 0.01

    suspend fun execute(
        year: Int,
        month: Int,
        totalIncome: Double,
        period: String, // Добавили период
        limits: List<BudgetLimitData>
    ): Result<Unit> {

        // 1. Валидация: Проверка распределения процентов
        val percentLimits = limits.filter { it.limitType == "PERCENT" }
        val sumOfPercents = percentLimits.sumOf { it.limitValue }

        if (sumOfPercents > 100.0 + VALIDATION_TOLERANCE) {
            return Result.failure(IllegalStateException("Сумма процентных лимитов превышает 100%."))
        }

        // 2. Валидация: Проверка распределения сумм
        val amountLimits = limits.filter { it.limitType == "AMOUNT" }
        val sumOfAmounts = amountLimits.sumOf { it.limitValue }

        val remainingIncomeForAmount = totalIncome * (1.0 - (sumOfPercents / 100.0))

        if (sumOfAmounts > remainingIncomeForAmount + VALIDATION_TOLERANCE) {
            return Result.failure(IllegalStateException("Сумма лимитов (в рублях) превышает доступный доход."))
        }

        // 5. Вызов репозитория для сохранения
        return repository.saveBudget(year, month, totalIncome, period, limits)
    }
}