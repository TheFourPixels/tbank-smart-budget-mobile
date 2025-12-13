package com.tbank.smartbudget.presentation

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tbank.smartbudget.presentation.ui.budget_tab.BudgetTabScreen
import com.tbank.smartbudget.presentation.ui.category_search.CategorySearchScreen
import com.tbank.smartbudget.presentation.ui.budget_details.BudgetDetailsScreen
import com.tbank.smartbudget.presentation.ui.budget_edit.BudgetEditScreen
import com.tbank.smartbudget.presentation.ui.all_operations.AllOperationsScreen

// Определяем маршруты для навигации
object Routes {
    const val BUDGET_TAB = "budget_tab"
    const val CATEGORY_SEARCH = "category_search"
    const val BUDGET_DETAILS = "budget_details"
    const val BUDGET_EDIT = "budget_edit"
    const val ALL_OPERATIONS = "all_operations" // Новый маршрут
}

/**
 * Главный контейнер навигации для всего приложения SmartBudget.
 */
@Composable
fun SmartBudgetNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.BUDGET_TAB
    ) {
        // 1. Экран вкладки бюджета
        composable(Routes.BUDGET_TAB) {
            BudgetTabScreen(
                // Переход на экран поиска
                onSearchClick = {
                    navController.navigate(Routes.CATEGORY_SEARCH)
                },
                // Переход к деталям бюджета
                onBudgetClick = {
                    navController.navigate(Routes.BUDGET_DETAILS)
                },
                // Переход ко всем операциям
                onAllOperationsClick = {
                    navController.navigate(Routes.ALL_OPERATIONS)
                }
            )
        }

        // 2. Экран поиска категорий
        composable(
            Routes.CATEGORY_SEARCH,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            CategorySearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // 3. Экран деталей бюджета
        composable(
            Routes.BUDGET_DETAILS,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            BudgetDetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEditClick = {
                    navController.navigate(Routes.BUDGET_EDIT)
                }
            )
        }

        // 4. Экран редактирования бюджета
        composable(
            Routes.BUDGET_EDIT,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            BudgetEditScreen(
                onNavigateBack = { navController.popBackStack() },
                onAddCategoryClick = { navController.navigate(Routes.CATEGORY_SEARCH) }
            )
        }

        // 5. Экран всех операций (Новый)
        composable(
            Routes.ALL_OPERATIONS,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            AllOperationsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}