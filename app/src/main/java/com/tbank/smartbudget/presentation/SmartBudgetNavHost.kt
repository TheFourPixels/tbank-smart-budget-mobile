package com.tbank.smartbudget.presentation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tbank.smartbudget.presentation.ui.all_operations.AllOperationsScreen
import com.tbank.smartbudget.presentation.ui.all_operations.AllOperationsViewModel
import com.tbank.smartbudget.presentation.ui.auth.AuthViewModel
import com.tbank.smartbudget.presentation.ui.auth.EnterPinScreen
import com.tbank.smartbudget.presentation.ui.auth.LoginEmailScreen
import com.tbank.smartbudget.presentation.ui.auth.LoginPasswordScreen
import com.tbank.smartbudget.presentation.ui.budget_details.BudgetDetailsScreen
import com.tbank.smartbudget.presentation.ui.budget_edit.BudgetEditScreen
import com.tbank.smartbudget.presentation.ui.budget_tab.BudgetTabScreen
import com.tbank.smartbudget.presentation.ui.category_search.CategorySearchScreen
import com.tbank.smartbudget.presentation.ui.selected_categories.SelectedCategoriesScreen

object Routes {
    // Auth Routes
    const val LOGIN_EMAIL = "login_email"
    // Маршрут с параметрами: email (обязательно), isExisting (boolean), userName (опционально)
    const val LOGIN_PASSWORD = "login_password/{email}/{isExisting}?userName={userName}"
    const val ENTER_PIN = "enter_pin"

    // Main App Routes
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
        startDestination = Routes.LOGIN_EMAIL
    ) {
        // --- AUTH FLOW ---

        composable(Routes.LOGIN_EMAIL) {
            // Здесь своя ViewModel
            val viewModel = hiltViewModel<AuthViewModel>()
            LoginEmailScreen(
                viewModel = viewModel,
                onNavigateNext = { email, isExisting, userName ->
                    // Формируем маршрут с параметрами
                    val route = "login_password/$email/$isExisting?userName=${userName ?: ""}"
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = Routes.LOGIN_PASSWORD,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("isExisting") { type = NavType.BoolType },
                navArgument("userName") { type = NavType.StringType; defaultValue = "" }
            ),
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } }
        ) { backStackEntry ->
            // Получаем аргументы
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val isExisting = backStackEntry.arguments?.getBoolean("isExisting") ?: false
            val userName = backStackEntry.arguments?.getString("userName").takeIf { it?.isNotEmpty() == true }

            // Создаем ViewModel для этого экрана и инициализируем её данными
            val viewModel = hiltViewModel<AuthViewModel>()

            // Важный момент: нужно передать эти данные во ViewModel, чтобы она знала контекст
            // Мы можем сделать это через LaunchedEffect в самом экране или метод инициализации

            LoginPasswordScreen(
                email = email,
                isUserExisting = isExisting,
                userName = userName,
                viewModel = viewModel,
                onNavigateNext = {
                    navController.navigate(Routes.ENTER_PIN)
                }
            )
        }

        composable(
            route = Routes.ENTER_PIN,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } }
        ) {
            EnterPinScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.BUDGET_TAB) {
                        popUpTo(Routes.LOGIN_EMAIL) { inclusive = true }
                    }
                }
            )
        }

        // --- MAIN FLOW ---

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
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { it } }
        ) {
            CategorySearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onCategoryClick = { categoryName ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_category_name", categoryName)
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.BUDGET_DETAILS) {
            BudgetDetailsScreen(
                onNavigateBack = { navController.popBackStack() },
                onEditClick = { navController.navigate(Routes.BUDGET_EDIT) }
            )
        }

        composable(Routes.BUDGET_EDIT) {
            BudgetEditScreen(
                onNavigateBack = { navController.popBackStack() },
                onAddCategoryClick = { navController.navigate(Routes.SELECTED_CATEGORIES) }
            )
        }

        composable(Routes.ALL_OPERATIONS) { backStackEntry ->
            val viewModel: AllOperationsViewModel = hiltViewModel()
            val selectedCategoryName = backStackEntry.savedStateHandle.get<String>("selected_category_name")

            androidx.compose.runtime.LaunchedEffect(selectedCategoryName) {
                selectedCategoryName?.let {
                    viewModel.onCategorySearchResult(it)
                    backStackEntry.savedStateHandle.remove<String>("selected_category_name")
                }
            }

            AllOperationsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSearchClick = { navController.navigate(Routes.CATEGORY_SEARCH) },
                viewModel = viewModel
            )
        }

        composable(Routes.SELECTED_CATEGORIES) {
            SelectedCategoriesScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}