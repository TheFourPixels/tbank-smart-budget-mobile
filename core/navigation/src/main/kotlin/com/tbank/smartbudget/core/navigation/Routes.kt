package com.tbank.smartbudget.core.navigation

import kotlinx.serialization.Serializable

// --- Auth Flow ---
@Serializable data object LoginEmailRoute
@Serializable data class LoginPasswordRoute(val email: String, val isExisting: Boolean, val userName: String? = null)
@Serializable data object EnterPinRoute

// --- Main Flow ---
@Serializable data object BudgetTabRoute
@Serializable data object CategorySearchRoute
@Serializable data object BudgetDetailsRoute
@Serializable data object BudgetEditRoute
@Serializable data object BudgetDashboardRoute
@Serializable data object PlanVsFactRoute
@Serializable data object AllOperationsRoute
@Serializable data object SelectedCategoriesRoute
@Serializable data object CategoriesDashboardRoute
@Serializable data object GoalsRoute
@Serializable data object AddGoalRoute
@Serializable data class GoalDetailsRoute(val goalId: Long)
@Serializable data class ContributeGoalRoute(
    val goalId: Long, 
    val recommendedAmount: Double,
    val targetAmount: Double,
    val savedAmount: Double
)
@Serializable data object ProfileRoute
@Serializable data object AddTransactionRoute
