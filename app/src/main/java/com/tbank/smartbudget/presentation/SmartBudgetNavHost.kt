package com.tbank.smartbudget.presentation

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tbank.smartbudget.presentation.ui.budget_tab.BudgetTabScreen
import com.tbank.smartbudget.presentation.ui.category_search.CategorySearchScreen

// Определяем маршруты для навигации
object Routes {
    const val BUDGET_TAB = "budget_tab"
    const val CATEGORY_SEARCH = "category_search"
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
                // Переход на экран поиска при нажатии на поле поиска
                onSearchClick = {
                    navController.navigate(Routes.CATEGORY_SEARCH)
                }
            )
        }

        // 2. Экран поиска категорий (С кастомной анимацией выезда снизу)
        composable(
            Routes.CATEGORY_SEARCH,
            // Анимация ВХОДА: экран выезжает снизу
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }, // Начинаем снизу
                )
            },
            // Анимация ВЫХОДА (когда переходим с этого экрана на другой)
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight } // Уезжаем вниз
                )
            },
            // Анимация ВОЗВРАТА (когда нажимаем "Назад")
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight } // Уезжаем вниз
                )
            },
            // Анимация ВХОДА (когда возвращаемся с другого экрана)
            popEnterTransition = {
                slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight } // Начинаем снизу
                )
            }
        ) {
            CategorySearchScreen(
                // При нажатии "Отменить" возвращаемся назад
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}