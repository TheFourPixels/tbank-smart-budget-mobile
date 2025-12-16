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
import com.tbank.smartbudget.presentation.ui.selected_categories.SelectedCategoriesScreen

object Routes {
    const val BUDGET_TAB = "budget_tab"
    const val CATEGORY_SEARCH = "category_search"
    const val BUDGET_DETAILS = "budget_details"
    const val BUDGET_EDIT = "budget_edit"
    const val ALL_OPERATIONS = "all_operations"
    const val SELECTED_CATEGORIES = "selected_categories"
}

@Composable
fun SmartBudgetNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.BUDGET_TAB
    ) {
        composable(Routes.BUDGET_TAB) {
            BudgetTabScreen(
                onSearchClick = { navController.navigate(Routes.CATEGORY_SEARCH) },
                onBudgetClick = { navController.navigate(Routes.BUDGET_DETAILS) },
                onAllOperationsClick = { navController.navigate(Routes.ALL_OPERATIONS) },
                onSelectedCategoriesClick = { navController.navigate(Routes.SELECTED_CATEGORIES) }
            )
        }

        composable(
            Routes.CATEGORY_SEARCH,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            CategorySearchScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            Routes.BUDGET_DETAILS,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            BudgetDetailsScreen(
                onNavigateBack = { navController.popBackStack() },
                onEditClick = { navController.navigate(Routes.BUDGET_EDIT) }
            )
        }

        // --- ЭКРАН РЕДАКТИРОВАНИЯ ---
        composable(
            Routes.BUDGET_EDIT,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            BudgetEditScreen(
                onNavigateBack = { navController.popBackStack() },
                // *** ИЗМЕНЕНИЕ: Теперь кнопка "Добавить категорию" ведет на экран выбора ***
                onAddCategoryClick = { navController.navigate(Routes.SELECTED_CATEGORIES) }
            )
        }

        composable(
            Routes.ALL_OPERATIONS,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            AllOperationsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            Routes.SELECTED_CATEGORIES,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            SelectedCategoriesScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}