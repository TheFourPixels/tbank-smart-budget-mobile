package com.tbank.smartbudget

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.example.smartbudget.feature.category_search.CategorySearchScreen
import com.example.smartbudget.feature.operations.AllOperationsScreen
import com.example.smartbudget.feature.dashboard.BudgetDashboardScreen
import com.example.smartbudget.feature.dashboard.categories.CategoriesDashboardScreen
import com.example.smartbudget.feature.dashboard.plan_vs_fact.PlanVsFactScreen
import com.tbank.smartbudget.core.navigation.AllOperationsRoute
import com.tbank.smartbudget.core.navigation.BudgetDashboardRoute
import com.tbank.smartbudget.core.navigation.BudgetDetailsRoute
import com.tbank.smartbudget.core.navigation.BudgetEditRoute
import com.tbank.smartbudget.core.navigation.BudgetTabRoute
import com.tbank.smartbudget.core.navigation.CategoriesDashboardRoute
import com.tbank.smartbudget.core.navigation.CategorySearchRoute
import com.tbank.smartbudget.core.navigation.EnterPinRoute
import com.tbank.smartbudget.core.navigation.LoginEmailRoute
import com.tbank.smartbudget.core.navigation.LoginPasswordRoute
import com.tbank.smartbudget.core.navigation.PlanVsFactRoute
import com.tbank.smartbudget.core.navigation.ProfileRoute
import com.tbank.smartbudget.core.navigation.SelectedCategoriesRoute
import com.tbank.smartbudget.feature.auth.AuthViewModel
import com.tbank.smartbudget.feature.auth.EnterPinScreen
import com.tbank.smartbudget.feature.auth.LoginEmailScreen
import com.tbank.smartbudget.feature.auth.LoginPasswordScreen
import com.tbank.smartbudget.feature.selected_categories.SelectedCategoriesScreen
import com.example.smartbudget.feature.operations.AllOperationsViewModel
import com.tbank.smartbudget.feature.budget_details.BudgetDetailsScreen
import com.tbank.smartbudget.feature.budget_tab.BudgetTabScreen
import com.tbank.smartbudget.feature.profile.ProfileScreen

@Composable
fun SmartBudgetNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginEmailRoute
    ) {
        // --- AUTH FEATURE ---
        composable<LoginEmailRoute> {
            val viewModel = hiltViewModel<AuthViewModel>()
            LoginEmailScreen(
                viewModel = viewModel,
                onNavigateNext = { email, isExisting, userName ->
                    navController.navigate(LoginPasswordRoute(email, isExisting, userName))
                }
            )
        }

        composable<LoginPasswordRoute>(
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } }
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<LoginPasswordRoute>()
            val viewModel = hiltViewModel<AuthViewModel>()

            LoginPasswordScreen(
                email = args.email,
                isUserExisting = args.isExisting,
                userName = args.userName,
                viewModel = viewModel,
                onSuccess = { navController.navigate(EnterPinRoute) },
                onBack = { navController.popBackStack() }
            )
        }

        composable<EnterPinRoute> {
            val viewModel = hiltViewModel<AuthViewModel>()
            EnterPinScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(BudgetTabRoute) {
                        popUpTo(EnterPinRoute) { inclusive = true }
                    }
                }
            )
        }

        // --- HOME / TABS FEATURE ---
        composable<BudgetTabRoute> {
            BudgetTabScreen(
                onSearchClick = { navController.navigate(CategorySearchRoute) },
                onProfileClick = { navController.navigate(ProfileRoute) },
                onBudgetClick = { navController.navigate(BudgetDetailsRoute) },
                onAllOperationsClick = { navController.navigate(AllOperationsRoute) },
                onSelectedCategoriesClick = { navController.navigate(SelectedCategoriesRoute) }
            )
        }

        // --- CATEGORY SEARCH FEATURE ---
        composable<CategorySearchRoute> {
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

        // --- BUDGET DETAILS & EDIT FEATURE ---
        composable<BudgetDetailsRoute> {
            BudgetDetailsScreen(
                onNavigateBack = { navController.popBackStack() },
                onEditClick = { navController.navigate(BudgetEditRoute) },
                onCalculationsClick = { navController.navigate(BudgetDashboardRoute) }
            )
        }

        composable<BudgetEditRoute> {
            BudgetEditScreen(
                onNavigateBack = { navController.popBackStack() },
                onAddCategoryClick = { navController.navigate(SelectedCategoriesRoute) }
            )
        }

        composable<SelectedCategoriesRoute> {
            SelectedCategoriesScreen(onNavigateBack = { navController.popBackStack() })
        }

        // --- DASHBOARD FEATURE ---
        composable<BudgetDashboardRoute>(
            enterTransition = { slideInHorizontally { it } }
        ) {
            BudgetDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlanVsFact = { navController.navigate(PlanVsFactRoute) },
                onNavigateToCategoriesDashboard = { navController.navigate(CategoriesDashboardRoute) }
            )
        }

        composable<PlanVsFactRoute> {
            PlanVsFactScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable<CategoriesDashboardRoute> {
            CategoriesDashboardScreen(onNavigateBack = { navController.popBackStack() })
        }

        // --- OPERATIONS FEATURE ---
        composable<AllOperationsRoute> { backStackEntry ->
            val viewModel = hiltViewModel<AllOperationsViewModel>()
            val selectedCategoryName = backStackEntry.savedStateHandle.get<String>("selected_category_name")

            LaunchedEffect(selectedCategoryName) {
                selectedCategoryName?.let {
                    viewModel.onCategorySearchResult(it)
                    backStackEntry.savedStateHandle.remove<String>("selected_category_name")
                }
            }
            AllOperationsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSearchClick = { navController.navigate(CategorySearchRoute) },
                viewModel = viewModel
            )
        }

        // --- PROFILE FEATURE ---
        composable<ProfileRoute> {
            ProfileScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun BudgetEditScreen(onNavigateBack: () -> Boolean, onAddCategoryClick: () -> Unit) {
    TODO("Not yet implemented")
}