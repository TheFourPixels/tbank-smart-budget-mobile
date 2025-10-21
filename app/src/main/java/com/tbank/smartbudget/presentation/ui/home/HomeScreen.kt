/*
package com.tbank.smartbudget.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

*/
/**
 * Главный экран раздела "Бюджет".
 *//*

@OptIn(ExperimentalMaterial3Api::class) // <--- Добавлено для подавления предупреждения Scaffold/TopAppBar
@Composable
fun HomeScreen(
    viewModel: BudgetViewModel = hiltViewModel()
) {
    // 1. Сбор состояния
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            BudgetTopBar(
                period = state.period.name, // "MONTH" или "YEAR"
                onEditClicked = viewModel::onEditClicked // Обработчик кнопки редактирования
            )
        }
    ) { paddingValues ->

        // 2. Отображение UI на основе состояния
        when {
            state.isLoading -> LoadingView(paddingValues)
            state.error != null -> ErrorView(state.error, viewModel::loadData, paddingValues)
            else -> BudgetContent(state, paddingValues)
        }
    }
}

// --- Компоненты UI (вложенные) ---

@OptIn(ExperimentalMaterial3Api::class) // <--- Добавлено для TopAppBar
@Composable
fun BudgetTopBar(period: String, onEditClicked: () -> Unit) {
    TopAppBar(
        title = { Text("Бюджет на $period") },
        actions = {
            IconButton(onClick = onEditClicked) {
                Icon(Icons.Filled.Edit, contentDescription = "Редактировать бюджет")
            }
        }
    )
}

@Composable
fun LoadingView(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(error: String?, onRetry: () -> Unit, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Произошла ошибка: $error", color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

@Composable
fun BudgetContent(state: BudgetUiState, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item {
            // Сводная информация
            state.summary?.let { summary ->
                SummaryCard(summary)
            }
        }

        item {
            // Настройки автозапуска и подтверждения
            SettingsSection()
        }

        item {
            // Заголовок для категорий
            Text(
                text = "Детализация по категориям",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        // 3. Список категорий
        items(state.categories) { category ->
            CategoryItem(category)
        }
    }
}

@Composable
fun SummaryCard(summary: BudgetSummaryUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Общий доход: ${summary.totalIncome}")
            Text("Лимит расходов: ${summary.totalLimit}")
            // Свободные средства могут быть выделены цветом
            Text("Свободные средства: ${summary.freeFunds}")
        }
    }
}

@Composable
fun CategoryItem(category: CategoryUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { */
/* Навигация к деталям категории *//*
 }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка и название
            // Icon(painterResource(id = category.iconRes), contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(category.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = "Потрачено ${category.spentValue} / Лимит ${category.limitValue} ₽",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Прогресс-бар и процент
            Column(horizontalAlignment = Alignment.End) {
                LinearProgressIndicator(
                    progress = category.progress,
                    color = Color(category.color), // Используем цвет из UI-модели
                    modifier = Modifier.width(100.dp)
                )
                Text(
                    text = "${(category.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun SettingsSection() {
    // Реализация переключателей для автозапуска и подтверждения
    // ...
}
*/
