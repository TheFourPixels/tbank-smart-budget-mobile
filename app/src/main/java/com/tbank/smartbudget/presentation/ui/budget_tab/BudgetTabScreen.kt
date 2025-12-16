package com.tbank.smartbudget.presentation.ui.budget_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.tbank.smartbudget.presentation.ui.budget_tab.components.BudgetSummaryCard
import com.tbank.smartbudget.presentation.ui.budget_tab.components.SummaryCategoryUi
import com.tbank.smartbudget.presentation.ui.budget_tab.components.SummaryRow
import com.tbank.smartbudget.presentation.ui.budget_tab.components.UserInfoAndSearch
import com.tbank.smartbudget.presentation.ui.budget_tab.components.WhiteBackgroundContainer
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun BudgetTabScreen(
    viewModel: BudgetViewModel = hiltViewModel(), // Используем реальный ViewModel
    onBudgetClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAllOperationsClick: () -> Unit = {},
    onSelectedCategoriesClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Логика обновления данных при возврате на экран
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    // --- ДИЗАЙН: Белый контейнер сверху ---
                    WhiteBackgroundContainer {
                        Column {
                            // 1. Профиль и Поиск
                            UserInfoAndSearch(
                                userName = state.userName,
                                onSearchClick = onSearchClick
                            )

                            Spacer(Modifier.height(16.dp))

                            // 2. Карточка "Кубышка"
                            val balance = state.summary?.totalIncome ?: "0 ₽"

                            BudgetSummaryCard(
                                budgetName = state.budgetName,
                                balance = balance,
                                term = state.budgetTerm,
                                onClick = onBudgetClick
                            )
                            Spacer(Modifier.height(21.dp))
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(24.dp))

                    // Маппинг данных для функционала иконок
                    val summaryCategories = state.categories.map {
                        SummaryCategoryUi(
                            id = it.id,
                            name = it.name,
                            iconRes = it.iconRes,
                            color = it.color
                        )
                    }

                    val totalSpent = state.summary?.totalSpent ?: "0 ₽"

                    // --- 3. Сводные карточки (Функционал с иконками) ---
                    SummaryRow(
                        totalSpent = totalSpent,
                        totalSpentDescription = "Трат в декабре",
                        categories = summaryCategories,
                        onAllOperationsClick = onAllOperationsClick,
                        onSelectedCategoriesClick = onSelectedCategoriesClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetTabScreenLightPreview() {
    SmartBudgetTheme(darkTheme = false) {
        // В превью hiltViewModel работать не будет без мока
    }
}