package com.tbank.smartbudget.presentation.ui.all_operations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.all_operations.components.BasicSearchBar
import com.tbank.smartbudget.presentation.ui.all_operations.components.CategoryTag
import com.tbank.smartbudget.presentation.ui.all_operations.components.DonutChart
import com.tbank.smartbudget.presentation.ui.all_operations.components.FilterChip
import com.tbank.smartbudget.presentation.ui.all_operations.components.PeriodToggle
import com.tbank.smartbudget.presentation.ui.all_operations.components.TransactionGroup
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@OptIn(ExperimentalLayoutApi::class) // Для FlowRow
@Composable
fun AllOperationsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AllOperationsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Шапка: Кнопка Назад + Поиск
            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Кнопка Назад
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.Black // Черный цвет на белом фоне
                        )
                    }

                    Spacer(Modifier.width(4.dp))

                    // Поле поиска (растягивается)
                    BasicSearchBar(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 2. Фильтры
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(text = state.selectedMonth, isBlue = true)
                    FilterChip(text = state.selectedFilter, isBlue = false)
                }
            }

            // 3. Общая сумма
            item {
                Text(
                    text = state.totalAmount,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )
                Text(
                    text = "Траты",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // 4. Круговая диаграмма
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DonutChart(
                        modifier = Modifier.size(200.dp),
                        values = listOf(30f, 25f, 20f, 15f, 10f),
                        colors = listOf(
                            Color(0xFFEF5350),
                            Color(0xFF5C6BC0),
                            Color(0xFFEC407A),
                            Color(0xFFFFA726),
                            Color(0xFFB0BEC5)
                        )
                    )
                }
            }

            // 5. Переключатель периода (Нед/Мес)
            item {
                PeriodToggle(
                    selectedType = state.periodType,
                    onTypeSelected = viewModel::onPeriodChanged
                )
            }

            // 6. Теги категорий (FlowRow для переноса строк)
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.categories.forEach { category ->
                        CategoryTag(category)
                    }
                }
            }

            // 7. Список транзакций
            items(state.transactions) { group ->
                TransactionGroup(group)
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllOperationsScreenPreview() {
    SmartBudgetTheme {
        AllOperationsScreen(onNavigateBack = {})
    }
}