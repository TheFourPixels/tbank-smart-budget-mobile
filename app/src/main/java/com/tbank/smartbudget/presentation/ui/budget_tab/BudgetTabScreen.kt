package com.tbank.smartbudget.presentation.ui.budget_tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.setup.BudgetSetupViewModel
import com.tbank.smartbudget.presentation.ui.setup.BudgetTabCategoryUi
import com.tbank.smartbudget.presentation.ui.theme.PrimaryDark
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme


@Composable
fun BudgetTabScreen(
    viewModel: BudgetSetupViewModel = hiltViewModel(),
    onBudgetClick: () -> Unit = {} // Для перехода к деталям/редактированию
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
                    selectedCategories = state.selectedCategories
                )
            }
        }
    }
}

// --- Компоненты UI ---

@Composable
fun WhiteBackgroundContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // Закругляем только нижние углы
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        // Тень для визуального отделения от серого фона Scaffold
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        content()
    }
}
@Composable
fun BasicSearchBar(
    searchText: String, // Текущее состояние текста
    onSearchTextChange: (String) -> Unit, // Функция для обновления состояния
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE0E0E0) // Устанавливаем разумное значение по умолчанию
) {
    // Стиль текста (для value и placeholder)
    val textStyle = TextStyle(
        color = Color.Black,
        fontSize = 15.sp
    )

    Box(
        modifier = modifier // Принимаем модификатор от родителя
            .height(35.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            // Иконка
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Поиск",
                tint = Color.DarkGray.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )

            // Основное поле ввода
            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange, // Используем функцию, переданную снаружи
                singleLine = true,
                textStyle = textStyle,
                modifier = Modifier.weight(1f),

                // Плейсхолдер
                decorationBox = { innerTextField ->
                    Box {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Поиск",
                                style = textStyle.copy(color = Color.DarkGray.copy(alpha = 0.6f)),
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun UserInfoAndSearch(userName: String) {
    // 1. Управление состоянием поиска внутри родителя (Hoisting State)
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        // Профиль (Аватар + Имя)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
                .clickable { /* Перейти в профиль */ }
        ) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .clip(CircleShape)
                    .background(PrimaryDark.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                // Заглушка для аватара
                Text(
                    text = userName.first().toString(),
                    color = PrimaryDark,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(5.dp))
            Text(
                modifier =  Modifier.padding(start = 5.dp),
                text = userName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = W700),
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "Профиль",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp).padding(start = 4.dp),
            )
        }

        Spacer(Modifier.height(8.dp))

        // 2. Вызов переиспользуемого компонента поиска
        BasicSearchBar(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            backgroundColor = SmartBudgetTheme.colors.lightGray, // Цвет вашей темы
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        )
    }
}

@Composable
fun BudgetSummaryCard(budgetName: String, balance: String, term: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        // Используем Brush для создания градиента как в макете
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                            colors = listOf(SmartBudgetTheme.colors.gradientDarkBlue, SmartBudgetTheme.colors.gradientGreen)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Бюджет “$budgetName”",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text("Баланс", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.7f))
                        Text(
                            balance,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(18.dp))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text("Срок", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.7f))
                        Text(
                            term,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(totalSpent: String, totalSpentDescription: String, selectedCategories: List<BudgetTabCategoryUi>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Карточка "Все операции"
        SummarySmallCard (modifier = Modifier.weight(1f)) {
            Text("Все операции", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(6.dp))
            Text("$totalSpentDescription\n$totalSpent",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 23.sp,
                fontSize = 16.sp)
            Spacer(Modifier.height(20.dp))
            // Имитация прогресс-бара
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.error)
            )
        }

        // Карточка "Выбранные категории"
        SummarySmallCard(modifier = Modifier.weight(1f)) {
            Text("Выбранные категории", style = MaterialTheme.typography.titleMedium.copy(fontWeight = W700, lineHeight = 23.sp))
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                selectedCategories.take(2).forEach { category ->
                    CategoryIconPlaceholder(Color(category.color))
                }
            }
        }
    }
}

@Composable
fun SummarySmallCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.height(height = 129.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp, // Используйте значение от 1.dp (легкая тень) до 24.dp (сильная тень)
            pressedElevation = 1.dp, // Тень при нажатии (эффект "утопания")
            // Настройка цвета тени для более мягкого эффекта, чем стандартный черный
        ),

    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun CategoryIconPlaceholder(color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        // Эмодзи-заглушка для иконки
        Text("🛒", fontSize = 16.sp)
    }
}

/*@Composable
fun CategoryOperationItem(category: BudgetTabCategoryUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { *//* Перейти к деталям категории *//* },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка категории
            CategoryIconPlaceholder(Color(category.color))

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = category.operationsCount,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}*/

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