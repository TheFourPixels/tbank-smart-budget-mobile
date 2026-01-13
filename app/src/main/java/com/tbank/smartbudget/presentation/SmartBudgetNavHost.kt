package com.tbank.smartbudget.presentation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.tbank.smartbudget.presentation.ui.budget_dashboard.BudgetDashboardScreen
import com.tbank.smartbudget.presentation.ui.budget_dashboard.categories.CategoriesDashboardScreen
import com.tbank.smartbudget.presentation.ui.budget_dashboard.plan_vs_fact.PlanVsFactScreen // Новый импорт
import com.tbank.smartbudget.presentation.ui.budget_details.BudgetDetailsScreen
import com.tbank.smartbudget.presentation.ui.budget_edit.BudgetEditScreen
import com.tbank.smartbudget.presentation.ui.budget_tab.BudgetTabScreen
import com.tbank.smartbudget.presentation.ui.category_search.CategorySearchScreen
import com.tbank.smartbudget.presentation.ui.profile.ProfileScreen
import com.tbank.smartbudget.presentation.ui.selected_categories.SelectedCategoriesScreen

object Routes {
    const val LOGIN_EMAIL = "login_email"
    const val LOGIN_PASSWORD = "login_password/{email}/{isExisting}?userName={userName}"
    const val ENTER_PIN = "enter_pin"

    const val BUDGET_TAB = "budget_tab"
    const val CATEGORY_SEARCH = "category_search"
    const val BUDGET_DETAILS = "budget_details"
    const val BUDGET_EDIT = "budget_edit"
    const val BUDGET_DASHBOARD = "budget_dashboard"
    const val PLAN_VS_FACT = "plan_vs_fact" // Новый маршрут
    const val ALL_OPERATIONS = "all_operations"
    const val SELECTED_CATEGORIES = "selected_categories"
    const val  CATEGORIES_DASHBOARD = "categories_dashboard"
    const val PROFILE = "profile"
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
            val viewModel = hiltViewModel<AuthViewModel>()
            LoginEmailScreen(
                viewModel = viewModel,
                onNavigateNext = { email, isExisting, userName ->
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
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val isExisting = backStackEntry.arguments?.getBoolean("isExisting") ?: false
            val userName = backStackEntry.arguments?.getString("userName").takeIf { it?.isNotEmpty() == true }
            val viewModel = hiltViewModel<AuthViewModel>()

            LoginPasswordScreen(
                email = email,
                isUserExisting = isExisting,
                userName = userName,
                viewModel = viewModel,
                onNavigateNext = { navController.navigate(Routes.ENTER_PIN) }
            )
        }

        composable(Routes.ENTER_PIN) {
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
                onProfileClick = {navController.navigate(Routes.PROFILE)},
                onBudgetClick = { navController.navigate(Routes.BUDGET_DETAILS) },
                onAllOperationsClick = { navController.navigate(Routes.ALL_OPERATIONS) },
                onSelectedCategoriesClick = { navController.navigate(Routes.SELECTED_CATEGORIES) }
            )
        }

        composable(Routes.CATEGORY_SEARCH) {
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
                onEditClick = { navController.navigate(Routes.BUDGET_EDIT) },
                onCalculationsClick = { navController.navigate(Routes.BUDGET_DASHBOARD) }
            )
        }

        composable(
            route = Routes.BUDGET_DASHBOARD,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { it } }
        ) {
            BudgetDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                // Передаем колбэк для навигации на План-Факт
                onNavigateToPlanVsFact = { navController.navigate(Routes.PLAN_VS_FACT) },
                onNavigateToCategoriesDashboard = { navController.navigate(Routes.CATEGORIES_DASHBOARD) }
            )
        }

        // Экран План-Факт
        composable(
            route = Routes.PLAN_VS_FACT,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { it } }
        ) {
            PlanVsFactScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CATEGORIES_DASHBOARD,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { it } }
        ) {
            CategoriesDashboardScreen(
                onNavigateBack = { navController.popBackStack() }
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

            LaunchedEffect(selectedCategoryName) {
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

        composable(Routes.PROFILE) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}