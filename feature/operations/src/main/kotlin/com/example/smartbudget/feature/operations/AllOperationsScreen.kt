package com.example.smartbudget.feature.operations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartbudget.feature.operations.components.*
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.TransactionId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllOperationsScreen(
    onNavigateBack: () -> Unit,
    onSearchClick: () -> Unit,
    viewModel: AllOperationsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDateRangePickerState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AllOperationsEffect.NavigateBack -> onNavigateBack()
                AllOperationsEffect.NavigateToSearch -> onSearchClick()
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    viewModel.onIntent(AllOperationsIntent.OnCustomDateRangeSelected(
                        datePickerState.selectedStartDateMillis,
                        datePickerState.selectedEndDateMillis
                    ))
                }) { Text("Выбрать") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Отмена") } }
        ) {
            DateRangePicker(
                state = datePickerState,
                modifier = Modifier.height(500.dp),
                title = { Text("Выберите период", modifier = Modifier.padding(16.dp)) },
                headline = {}
            )
        }
    }

    AllOperationsContent(
        state = state,
        onBackClick = { viewModel.onIntent(AllOperationsIntent.OnBackClick) },
        onCalendarClick = { showDatePicker = true },
        onSearchClick = { viewModel.onIntent(AllOperationsIntent.OnSearchClick) },
        onPeriodChanged = { viewModel.onIntent(AllOperationsIntent.OnPeriodChanged(it)) },
        onCategorySelected = { viewModel.onIntent(AllOperationsIntent.OnCategorySelected(it)) }
    )
}

@Composable
private fun AllOperationsContent(
    state: AllOperationsUiState,
    onBackClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onSearchClick: () -> Unit,
    onPeriodChanged: (PeriodType) -> Unit,
    onCategorySelected: (String) -> Unit
) {
    Scaffold(containerColor = Color.White) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SmartBudgetTheme.colors.blue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                    OperationsHeader(
                        onBackClick = onBackClick,
                        onCalendarClick = onCalendarClick,
                        onSearchClick = onSearchClick
                    )
                }

                item {
                    PeriodSummary(
                        dateRangeLabel = state.dateRangeLabel,
                        totalExpense = state.totalExpense
                    )
                }

                item {
                    OperationsChartSection(
                        chartData = state.chartData,
                        selectedCategoryNames = state.selectedCategoryNames,
                        periodType = state.periodType,
                        onPeriodChanged = onPeriodChanged,
                        onCategorySelected = onCategorySelected
                    )
                }

                if (state.transactionGroups.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("Нет операций", color = Color.Gray)
                        }
                    }
                } else {
                    items(state.transactionGroups) { group ->
                        TransactionGroup(group)
                    }
                }
                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}

@Preview(showBackground = true, name = "Default State")
@Composable
private fun AllOperationsScreenPreview() {
    SmartBudgetTheme {
        AllOperationsContent(
            state = AllOperationsUiState(
                dateRangeLabel = "1 дек — 31 дек",
                totalExpense = "45 000 ₽",
                chartData = listOf(
                    ChartDataUi("Еда", "15 000 ₽", Color.Red, 0.4f),
                    ChartDataUi("Транспорт", "5 000 ₽", Color.Blue, 0.15f),
                    ChartDataUi("Развлечения", "8 000 ₽", Color.Magenta, 0.2f)
                ),
                transactionGroups = listOf(
                    TransactionGroupUi(
                        dateHeader = "Сегодня",
                        dayTotal = "1 200 ₽",
                        items = listOf(
                            TransactionUi(
                                id = TransactionId(1),
                                title = "Пятерочка",
                                subtitle = "Еда",
                                amount = "1 200 ₽",
                                amountColor = Color.Black,
                                iconColor = Color.Red
                            )
                        )
                    ),
                    TransactionGroupUi(
                        dateHeader = "Вчера",
                        dayTotal = "3 500 ₽",
                        items = listOf(
                            TransactionUi(
                                id = TransactionId(2),
                                title = "Лукойл",
                                subtitle = "Транспорт",
                                amount = "3 500 ₽",
                                amountColor = Color.Black,
                                iconColor = Color.Blue
                            )
                        )
                    )
                )
            ),
            onBackClick = {},
            onCalendarClick = {},
            onSearchClick = {},
            onPeriodChanged = {},
            onCategorySelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun AllOperationsScreenEmptyPreview() {
    SmartBudgetTheme {
        AllOperationsContent(
            state = AllOperationsUiState(
                dateRangeLabel = "1 янв — 7 янв",
                totalExpense = "0 ₽",
                isLoading = false,
                transactionGroups = emptyList()
            ),
            onBackClick = {},
            onCalendarClick = {},
            onSearchClick = {},
            onPeriodChanged = {},
            onCategorySelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun AllOperationsScreenLoadingPreview() {
    SmartBudgetTheme {
        AllOperationsContent(
            state = AllOperationsUiState(isLoading = true),
            onBackClick = {},
            onCalendarClick = {},
            onSearchClick = {},
            onPeriodChanged = {},
            onCategorySelected = {}
        )
    }
}
