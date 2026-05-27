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
import com.example.smartbudget.feature.category_search.CategorySearchViewModel
import com.example.smartbudget.feature.operations.AllOperationsScreen
import com.example.smartbudget.feature.operations.AllOperationsViewModel
import com.example.smartbudget.feature.dashboard.BudgetDashboardScreen
import com.example.smartbudget.feature.dashboard.BudgetDashboardViewModel
import com.example.smartbudget.feature.dashboard.categories.CategoriesDashboardScreen
import com.example.smartbudget.feature.dashboard.categories.CategoriesDashboardViewModel
import com.example.smartbudget.feature.dashboard.goals.AddGoalScreen
import com.example.smartbudget.feature.dashboard.goals.AddGoalViewModel
import com.example.smartbudget.feature.dashboard.goals.ContributeGoalScreen
import com.example.smartbudget.feature.dashboard.goals.ContributeGoalViewModel
import com.example.smartbudget.feature.dashboard.goals.GoalDetailsScreen
import com.example.smartbudget.feature.dashboard.goals.GoalDetailsViewModel
import com.example.smartbudget.feature.dashboard.goals.GoalsScreen
import com.example.smartbudget.feature.dashboard.goals.GoalsViewModel
import com.example.smartbudget.feature.dashboard.plan_vs_fact.PlanVsFactScreen
import com.example.smartbudget.feature.dashboard.plan_vs_fact.PlanVsFactViewModel
import com.example.smartbudget.feature.operations.AllOperationsIntent
import com.example.smartbudget.feature.operations.AddTransactionScreen
import com.example.smartbudget.feature.operations.AddTransactionViewModel
import com.tbank.smartbudget.core.navigation.*
import com.tbank.smartbudget.feature.auth.*
import com.tbank.smartbudget.feature.budget_details.BudgetDetailsScreen
import com.tbank.smartbudget.feature.budget_edit.BudgetEditScreen
import com.tbank.smartbudget.feature.budget_edit.BudgetEditViewModel
import com.tbank.smartbudget.feature.budget_tab.BudgetTabScreen
import com.tbank.smartbudget.feature.budget_tab.BudgetViewModel
import com.tbank.smartbudget.feature.profile.ProfileScreen
import com.tbank.smartbudget.feature.profile.ProfileViewModel
import com.tbank.smartbudget.feature.selected_categories.SelectedCategoriesScreen
import com.tbank.smartbudget.feature.selected_categories.SelectedCategoriesViewModel

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
                },
                onBack = { navController.popBackStack() }
            )
        }

        // --- HOME / TABS FEATURE ---
        composable<BudgetTabRoute> {
            val viewModel = hiltViewModel<BudgetViewModel>()
            BudgetTabScreen(
                viewModel = viewModel,
                onNavigateToBudgetEdit = { navController.navigate(BudgetDetailsRoute) }, // Переход к деталям
                onNavigateToSearch = { navController.navigate(CategorySearchRoute) },
                onNavigateToProfile = { navController.navigate(ProfileRoute) },
                onNavigateToAllOperations = { navController.navigate(AllOperationsRoute) },
                onNavigateToSelectedCategories = { navController.navigate(SelectedCategoriesRoute) },
                onNavigateToAddTransaction = { navController.navigate(AddTransactionRoute) }
            )
        }

        composable<AddTransactionRoute> {
            val viewModel = hiltViewModel<AddTransactionViewModel>()
            AddTransactionScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSelectedCategories = { navController.navigate(SelectedCategoriesRoute) }
            )
        }

        // --- CATEGORY SEARCH FEATURE ---
        composable<CategorySearchRoute> {
            val viewModel = hiltViewModel<CategorySearchViewModel>()
            CategorySearchScreen(
                viewModel = viewModel,
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

        // ... остальные маршруты остаются без изменений
        composable<BudgetEditRoute> {
            val viewModel = hiltViewModel<BudgetEditViewModel>()
            BudgetEditScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onAddCategoryClick = { navController.navigate(SelectedCategoriesRoute) }
            )
        }

        composable<SelectedCategoriesRoute> {
            val viewModel = hiltViewModel<SelectedCategoriesViewModel>()
            SelectedCategoriesScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreate = { /* Реализовать переход на создание */ }
            )
        }

        // --- DASHBOARD FEATURE ---
        composable<BudgetDashboardRoute>(
            enterTransition = { slideInHorizontally { it } }
        ) {
            val viewModel = hiltViewModel<BudgetDashboardViewModel>()
            BudgetDashboardScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlanVsFact = { navController.navigate(PlanVsFactRoute) },
                onNavigateToCategoriesDashboard = { navController.navigate(CategoriesDashboardRoute) },
                onNavigateToGoals = { navController.navigate(GoalsRoute) }
            )
        }

        composable<GoalsRoute> {
            val viewModel = hiltViewModel<GoalsViewModel>()
            GoalsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddGoal = { navController.navigate(AddGoalRoute) },
                onNavigateToGoalDetails = { goalId -> 
                    navController.navigate(GoalDetailsRoute(goalId))
                }
            )
        }

        composable<GoalDetailsRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GoalDetailsRoute>()
            val viewModel = hiltViewModel<GoalDetailsViewModel>()
            GoalDetailsScreen(
                goalId = args.goalId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToContribute = { goalId, amount, target, saved ->
                    navController.navigate(ContributeGoalRoute(goalId, amount, target, saved))
                },
                viewModel = viewModel
            )
        }

        composable<ContributeGoalRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<ContributeGoalRoute>()
            val viewModel = hiltViewModel<ContributeGoalViewModel>()
            ContributeGoalScreen(
                goalId = args.goalId,
                recommendedAmount = args.recommendedAmount,
                targetAmount = args.targetAmount,
                savedAmount = args.savedAmount,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }

        composable<AddGoalRoute> {
            val viewModel = hiltViewModel<AddGoalViewModel>()
            AddGoalScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<PlanVsFactRoute> {
            val viewModel = hiltViewModel<PlanVsFactViewModel>()
            PlanVsFactScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CategoriesDashboardRoute> {
            val viewModel = hiltViewModel<CategoriesDashboardViewModel>()
            CategoriesDashboardScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- OPERATIONS FEATURE ---
        composable<AllOperationsRoute> { backStackEntry ->
            val viewModel = hiltViewModel<AllOperationsViewModel>()
            val selectedCategoryName = backStackEntry.savedStateHandle.get<String>("selected_category_name")

            LaunchedEffect(selectedCategoryName) {
                selectedCategoryName?.let {
                    viewModel.onIntent(AllOperationsIntent.OnCategorySearchResult(it))
                    backStackEntry.savedStateHandle.remove<String>("selected_category_name")
                }
            }
            AllOperationsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onSearchClick = { navController.navigate(CategorySearchRoute) },
                onAddTransactionClick = { navController.navigate(AddTransactionRoute) }
            )
        }

        // --- PROFILE FEATURE ---
        composable<ProfileRoute> {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToBudget = { budgetId ->
                    navController.navigate(BudgetDetailsRoute)
                }
            )
        }
    }
}
