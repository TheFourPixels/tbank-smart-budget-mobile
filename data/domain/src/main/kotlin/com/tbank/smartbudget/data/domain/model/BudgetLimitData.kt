package com.tbank.smartbudget.data.domain.model

/**
 * Модель данных для передачи обновленных лимитов при сохранении бюджета.
 * Используется в SaveBudgetUseCase и BudgetRepository.saveBudget().
 */
data class BudgetLimitData(
    val categoryId: Long,
    val limitValue: Double,
    val limitType: BudgetLimitType // "PERCENT" или "AMOUNT"
)