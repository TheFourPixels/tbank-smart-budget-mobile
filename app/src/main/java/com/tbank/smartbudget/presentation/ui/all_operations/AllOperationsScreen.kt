package com.tbank.smartbudget.presentation.ui.all_operations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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

// --- КОМПОНЕНТЫ ---

@Composable
fun BasicSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF5F5F5) // Светло-серый фон
) {
    val textStyle = TextStyle(
        color = Color.Black,
        fontSize = 16.sp
    )

    Box(
        modifier = modifier
            .height(44.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.width(8.dp))

            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                singleLine = true,
                textStyle = textStyle,
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    Box {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Поиск",
                                style = textStyle.copy(color = Color.Gray)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (searchText.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = "Очистить",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onSearchTextChange("") }
                )
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isBlue: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (isBlue) SmartBudgetTheme.colors.blue else Color(0xFFF5F5F5),
                shape = RoundedCornerShape(50) // Изменено на 50% (полностью круглые края)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp), // Увеличены отступы
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                color = if (isBlue) Color.White else Color.Black,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (isBlue) Color.White else Color.Black,
                modifier = Modifier.size(20.dp) // Чуть крупнее иконка
            )
        }
    }
}

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    colors: List<Color>,
    strokeWidth: Dp = 28.dp // Чуть тоньше для соответствия стилю
) {
    Canvas(modifier = modifier) {
        val total = values.sum()
        var startAngle = -90f

        values.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f
            val gap = 17f // Зазор между сегментами

            drawArc(
                color = colors.getOrElse(index) { Color.Gray },
                startAngle = startAngle + gap / 2,
                sweepAngle = sweepAngle - gap,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun PeriodToggle(
    selectedType: PeriodType,
    onTypeSelected: (PeriodType) -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color(0xFFF5F5F5), RoundedCornerShape(50)) // Серый фон
            .padding(4.dp)
    ) {
        Row {
            // Неделя
            Box(
                modifier = if (selectedType == PeriodType.WEEK) {
                    Modifier.weight(1f)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(50))
                        .background(Color.White, RoundedCornerShape(50)) // Белый активный
                        .clickable { onTypeSelected(PeriodType.WEEK) }
                        .padding(vertical = 8.dp)
                } else {
                    Modifier.weight(1f)
                        .clip(RoundedCornerShape(50))
                        .clickable { onTypeSelected(PeriodType.WEEK) }
                        .padding(vertical = 8.dp)
                },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Нед",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            // Месяц
            Box(
                modifier = if (selectedType == PeriodType.MONTH) {
                    Modifier.weight(1f)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(50))
                        .background(Color.White, RoundedCornerShape(50)) // Белый активный
                        .clickable { onTypeSelected(PeriodType.MONTH) }
                        .padding(vertical = 8.dp)
                } else {
                    Modifier.weight(1f)
                        .clip(RoundedCornerShape(50))
                        .clickable { onTypeSelected(PeriodType.MONTH) }
                        .padding(vertical = 8.dp)
                },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Мес",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun CategoryTag(category: OperationCategoryUi) {
    val categoryColor = Color(category.color)
    val backgroundColor = categoryColor.copy(alpha = 0.15f)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Цветной круг (иконка)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(categoryColor)
        )
        Spacer(Modifier.width(8.dp))
        // Текст категории и сумма
        Text(
            text = "${category.name} ${category.amount}",
            color = Color.Black.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TransactionGroup(group: TransactionGroupUi) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(group.date, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text(group.totalAmount, style = MaterialTheme.typography.titleMedium.copy(color = Color.LightGray))
        }
        Spacer(Modifier.height(16.dp))

        group.items.forEach { transaction ->
            TransactionItem(transaction)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(transaction.iconColor)),
            contentAlignment = Alignment.Center
        ) {
            // Заглушка
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.name, fontWeight = FontWeight.Bold)
            Text(transaction.categoryName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }

        Text(transaction.amount, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun AllOperationsScreenPreview() {
    SmartBudgetTheme {
        AllOperationsScreen(onNavigateBack = {})
    }
}