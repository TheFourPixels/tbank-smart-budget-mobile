package com.tbank.smartbudget.presentation.ui.budget_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.budget_tab.components.BudgetSummaryCard
import com.tbank.smartbudget.presentation.ui.budget_tab.components.SummaryRow
import com.tbank.smartbudget.presentation.ui.budget_tab.components.UserInfoAndSearch
import com.tbank.smartbudget.presentation.ui.budget_tab.components.WhiteBackgroundContainer
import com.tbank.smartbudget.presentation.ui.setup.BudgetSetupViewModel
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme


@Composable
fun BudgetTabScreen(
    viewModel: BudgetSetupViewModel = hiltViewModel(),
    onBudgetClick: () -> Unit = {}, // Для перехода к деталям/редактированию
    onSearchClick: () -> Unit = {}, // НОВЫЙ КОЛБЭК ДЛЯ ПЕРЕХОДА К ПОИСКУ
    onAllOperationsClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                WhiteBackgroundContainer {
                    Column {
                        // --- 1. Профиль и Поиск ---
                        UserInfoAndSearch(
                            userName = state.userName,
                            onSearchClick = onSearchClick // Передаем колбэк
                        )

                        Spacer(Modifier.height(16.dp))

                        // --- 2. Карточка "Кубышка" ---
                        BudgetSummaryCard(
                            budgetName = state.budgetName,
                            balance = state.budgetBalance,
                            term = state.budgetTerm,
                            onClick = onBudgetClick
                        )
                        Spacer(Modifier.height(21.dp))
                    }
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                // --- 3. Сводные карточки ---
                SummaryRow(
                    totalSpent = state.totalSpent,
                    totalSpentDescription = state.totalSpentDescription,
                    selectedCategories = state.selectedCategories,
                    onAllOperationsClick = onAllOperationsClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetTabScreenLightPreview() {
    SmartBudgetTheme(darkTheme = false) {
        BudgetTabScreen(viewModel = hiltViewModel()) // hiltViewModel() здесь просто заглушка для Preview
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetTabScreenDarkPreview() {
    SmartBudgetTheme(darkTheme = true) {
        BudgetTabScreen(viewModel = hiltViewModel())
    }
}