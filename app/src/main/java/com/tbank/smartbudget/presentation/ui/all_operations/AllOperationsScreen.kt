package com.tbank.smartbudget.presentation.ui.all_operations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.all_operations.components.CategoryTag
import com.tbank.smartbudget.presentation.ui.all_operations.components.DonutChart
import com.tbank.smartbudget.presentation.ui.all_operations.components.PeriodToggle
import com.tbank.smartbudget.presentation.ui.all_operations.components.TransactionGroup
import com.tbank.smartbudget.presentation.ui.common.BasicSearchBar
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AllOperationsScreen(
    onNavigateBack: () -> Unit,
    onSearchClick: () -> Unit, // Колбэк для перехода на экран поиска категорий
    viewModel: AllOperationsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Поисковый текст здесь может быть именем выбранной категории, если мы ищем по категории
    // Или пустым, если мы используем экран поиска только для выбора фильтра
    // Для простоты пока оставим пустым
    val searchText by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDateRangePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    viewModel.onCustomDateRangeSelected(datePickerState.selectedStartDateMillis, datePickerState.selectedEndDateMillis)
                }) { Text("Выбрать") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Отмена") } }
        ) {
            DateRangePicker(state = datePickerState, modifier = Modifier.height(500.dp), title = { Text("Выберите период", modifier = Modifier.padding(16.dp)) }, headline = {})
        }
    }

    Scaffold(containerColor = Color.White) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.Black)
                        }
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Календарь", tint = SmartBudgetTheme.colors.blue)
                        }
                        Spacer(Modifier.width(4.dp))

                        // Поле поиска, работающее как кнопка перехода
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSearchClick() } // Переход на экран поиска
                        ) {
                            // Используем BasicSearchBar, но блокируем ввод (в реальном приложении лучше добавить параметр enabled/readOnly в BasicSearchBar)
                            // Здесь мы просто накладываем прозрачный Box поверх, чтобы перехватить клик,
                            // или полагаемся на то, что BasicSearchBar сам по себе не перехватывает клики контейнера если не в фокусе.
                            // Для надежности лучше использовать Box с кликом.
                            BasicSearchBar(
                                searchText = searchText,
                                onSearchTextChange = { }, // Игнорируем ввод здесь
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = Color(0xFFF5F5F5)
                            )
                            // Прозрачная "шторка" для перехвата клика
                            Box(modifier = Modifier.matchParentSize().clickable { onSearchClick() })
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = state.dateRangeLabel, style = MaterialTheme.typography.labelLarge.copy(color = Color.Gray), modifier = Modifier.padding(start = 4.dp))
                    }
                }

                item {
                    Text(text = state.totalExpense, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, fontSize = 32.sp))
                    Text(text = "Траты за период", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }

                item {
                    Box(modifier = Modifier.fillMaxWidth().height(250.dp), contentAlignment = Alignment.Center) {
                        if (state.chartData.isNotEmpty()) {
                            DonutChart(modifier = Modifier.size(200.dp), chartData = state.chartData, selectedCategoryNames = state.selectedCategoryNames)
                        } else {
                            Text("Нет данных за этот период", color = Color.Gray)
                        }
                    }
                }

                item {
                    PeriodToggle(
                        selectedType = state.periodType,
                        onTypeSelected = viewModel::onPeriodChanged
                    )
                }

                item {
                    FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        state.chartData.forEach { chartItem ->
                            val isSelected = state.selectedCategoryNames.contains(chartItem.categoryName)
                            val isAnySelected = state.selectedCategoryNames.isNotEmpty()
                            val isDimmed = isAnySelected && !isSelected
                            CategoryTag(chartItem = chartItem, isSelected = isSelected, isDimmed = isDimmed, onClick = { viewModel.onCategorySelected(chartItem.categoryName) })
                        }
                    }
                }

                if (state.transactionGroups.isEmpty()) {
                    item { Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { Text("Нет операций", color = Color.Gray) } }
                } else {
                    items(state.transactionGroups) { group -> TransactionGroup(group) }
                }
                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}
