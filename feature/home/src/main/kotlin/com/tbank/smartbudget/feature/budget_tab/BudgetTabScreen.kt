package com.tbank.smartbudget.feature.budget_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.CategoryId
import com.tbank.smartbudget.feature.budget_tab.components.*

@Composable
fun BudgetTabScreen(
    viewModel: BudgetViewModel = hiltViewModel(),
    onNavigateToBudgetEdit: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAllOperations: () -> Unit,
    onNavigateToSelectedCategories: () -> Unit,
    onNavigateToAddTransaction: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                BudgetEffect.NavigateToBudgetEdit -> onNavigateToBudgetEdit()
                BudgetEffect.NavigateToSearch -> onNavigateToSearch()
                BudgetEffect.NavigateToProfile -> onNavigateToProfile()
                BudgetEffect.NavigateToAllOperations -> onNavigateToAllOperations()
                BudgetEffect.NavigateToSelectedCategories -> onNavigateToSelectedCategories()
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onIntent(BudgetIntent.OnRefresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BudgetTabContent(
        state = state,
        onSearchClick = { viewModel.onIntent(BudgetIntent.OnSearchClick) },
        onProfileClick = { viewModel.onIntent(BudgetIntent.OnProfileClick) },
        onBudgetClick = { viewModel.onIntent(BudgetIntent.OnBudgetClick) },
        onAllOperationsClick = { viewModel.onIntent(BudgetIntent.OnAllOperationsClick) },
        onSelectedCategoriesClick = { viewModel.onIntent(BudgetIntent.OnSelectedCategoriesClick) },
        onAddTransactionClick = onNavigateToAddTransaction
    )
}

@Composable
private fun BudgetTabContent(
    state: BudgetUiState,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBudgetClick: () -> Unit,
    onAllOperationsClick: () -> Unit,
    onSelectedCategoriesClick: () -> Unit,
    onAddTransactionClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick,
                containerColor = SmartBudgetTheme.colors.blue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить операцию")
            }
        }
    ) { paddingValues ->
        if (state.isLoading && state.summary == null && state.hasBudget) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SmartBudgetTheme.colors.blue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    WhiteBackgroundContainer {
                        Column {
                            UserInfoAndSearch(
                                userName = state.userName,
                                onSearchClick = onSearchClick,
                                onProfileClick = onProfileClick
                            )
                            Spacer(Modifier.height(16.dp))
                            BudgetSummaryCard(
                                hasBudget = state.hasBudget,
                                budgetName = state.budgetName,
                                balance = state.summary?.freeFunds ?: "0 ₽",
                                term = state.budgetTerm,
                                onClick = onBudgetClick
                            )
                            Spacer(Modifier.height(21.dp))
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(24.dp))
                    SummaryRow(
                        totalSpent = state.summary?.totalSpent ?: "0 ₽",
                        totalSpentDescription = "Трат в этом месяце",
                        spentProgress = state.summary?.progress ?: 0f,
                        categories = state.categories.map {
                            SummaryCategoryUi(it.id.value, it.name, it.iconRes, it.color)
                        },
                        onAllOperationsClick = onAllOperationsClick,
                        onSelectedCategoriesClick = onSelectedCategoriesClick
                    )
                    Spacer(Modifier.height(24.dp))
                }

                if (state.hasBudget && state.categories.isNotEmpty()) {
                    item {
                        Text(
                            text = "Лимиты по категориям",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    items(state.categories, key = { it.id.value }) { category ->
                        CategoryProgressItem(
                            category = category,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                } else if (!state.hasBudget) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Планируйте свои расходы, чтобы накопить на мечту!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetTabScreenPreview() {
    SmartBudgetTheme {
        BudgetTabContent(
            state = BudgetUiState(
                userName = "Иван",
                budgetName = "Мой бюджет",
                budgetTerm = "на 1 месяц",
                categories = listOf(
                    CategoryUi(
                        id = CategoryId(1),
                        name = "Продукты",
                        iconRes = 0,
                        color = 0xFF43A047,
                        spentValue = "5 000 ₽",
                        limitValue = "10 000 ₽",
                        progress = 0.5f
                    )
                )
            ),
            onSearchClick = {},
            onProfileClick = {},
            onBudgetClick = {},
            onAllOperationsClick = {},
            onSelectedCategoriesClick = {},
            onAddTransactionClick = {}
        )
    }
}
