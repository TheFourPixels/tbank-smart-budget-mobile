package com.tbank.smartbudget.presentation.ui.budget_tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.setup.BudgetSetupViewModel
import com.tbank.smartbudget.presentation.ui.setup.BudgetTabCategoryUi
import com.tbank.smartbudget.presentation.ui.theme.DarkOnSurface
import com.tbank.smartbudget.presentation.ui.theme.PrimaryDark
import com.tbank.smartbudget.presentation.ui.theme.SecondaryDark
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
                // --- 1. Профиль и Поиск ---
                UserInfoAndSearch(userName = state.userName)
            }

            item {
                Spacer(Modifier.height(16.dp))
                // --- 2. Карточка "Кубышка" ---
                BudgetSummaryCard(
                    budgetName = state.budgetName,
                    balance = state.budgetBalance,
                    term = state.budgetTerm,
                    onClick = onBudgetClick
                )
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

            item {
                Spacer(Modifier.height(24.dp))
                // --- 4. Список категорий ---
                Text(
                    text = "Категории",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DarkOnSurface,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            items(state.selectedCategories, key = { it.id }) { category ->
                CategoryOperationItem(category)
            }
        }
    }
}

// --- Компоненты UI ---
@Composable
fun UserInfoAndSearch(userName: String) {
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
                modifier =  Modifier.padding(start = 15.dp),
                text = userName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = W700),
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "Профиль",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(16.dp).padding(start = 4.dp),
            )
        }

        Spacer(Modifier.height(8.dp))

        // Поле поиска
        OutlinedTextField(
            value = "", // Состояние поиска
            onValueChange = { /* Обновление поиска */ },
            placeholder = { Text("Поиск") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Поиск") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(35.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SmartBudgetTheme.colors.lightGray,
                unfocusedContainerColor = SmartBudgetTheme.colors.lightGray,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
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
                        colors = listOf(SecondaryDark.copy(alpha = 0.8f), PrimaryDark.copy(alpha = 0.9f))
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Баланс", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.7f))
                        Text(
                            balance,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
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
            Spacer(Modifier.height(4.dp))
            Text("$totalSpentDescription\n$totalSpent",
                style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
//            Spacer(Modifier.height(33.dp))
            /*Text(
                totalSpent,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface // Цвет для трат
            )*/
            Spacer(Modifier.height(8.dp))
            // Имитация прогресс-бара
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
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
        modifier = modifier.height(IntrinsicSize.Min)
        ,
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

@Composable
fun CategoryOperationItem(category: BudgetTabCategoryUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { /* Перейти к деталям категории */ },
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