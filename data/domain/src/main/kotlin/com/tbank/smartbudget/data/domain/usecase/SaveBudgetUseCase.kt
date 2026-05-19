package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.data.domain.model.BudgetLimitData
import com.tbank.smartbudget.data.domain.model.BudgetLimitType
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import javax.inject.Inject

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
        period: String,
        limits: List<BudgetLimitData>
    ): Result<Unit> {
        if (totalIncome <= 0) {
            return Result.failure(IllegalStateException("Доход должен быть больше нуля."))
        }

        // 1. Валидация: Проверка распределения процентов
        val percentLimits = limits.filter { it.limitType == BudgetLimitType.PERCENT }
        val sumOfPercents = percentLimits.sumOf { it.limitValue }

        if (sumOfPercents > 100.0 + VALIDATION_TOLERANCE) {
            return Result.failure(IllegalStateException("Сумма процентных лимитов превышает 100%."))
        }

        // 2. Валидация: Проверка распределения сумм
        val amountLimits = limits.filter { it.limitType == BudgetLimitType.SUM }
        val sumOfAmounts = amountLimits.sumOf { it.limitValue }

        val remainingIncomeForAmount = totalIncome * (1.0 - (sumOfPercents / 100.0))

        if (sumOfAmounts > remainingIncomeForAmount + VALIDATION_TOLERANCE) {
            return Result.failure(IllegalStateException("Сумма лимитов (в рублях) превышает доступный доход."))
        }

        // 3. Вызов репозитория для сохранения
        return repository.saveBudget(year, month, totalIncome, period, limits)
    }
}
