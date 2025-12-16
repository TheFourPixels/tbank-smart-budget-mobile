package com.tbank.smartbudget.domain.model

/**
 * Модель данных для передачи обновленных лимитов при сохранении бюджета.
 * Используется в SaveBudgetUseCase и BudgetRepository.saveBudget().
 */
data class BudgetLimitData(
    val categoryId: Long,
    val limitValue: Double,
    val limitType: String // "PERCENT" или "AMOUNT"
)