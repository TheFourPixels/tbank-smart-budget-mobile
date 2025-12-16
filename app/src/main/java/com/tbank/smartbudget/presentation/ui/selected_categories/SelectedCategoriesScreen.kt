package com.tbank.smartbudget.presentation.ui.selected_categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.selected_categories.components.CategoryRowItem
import com.tbank.smartbudget.presentation.ui.selected_categories.components.SearchInputBar
import com.tbank.smartbudget.presentation.ui.selected_categories.components.ShadowCardContainer
import com.tbank.smartbudget.presentation.ui.selected_categories.components.SingleCategoryCard
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun SelectedCategoriesScreen(
    onNavigateBack: () -> Unit,
    viewModel: SelectedCategoriesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF8F8F8)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Поиск
            item {
                Spacer(Modifier.height(8.dp))
                SearchInputBar(
                    searchText = state.searchQuery,
                    onSearchTextChange = viewModel::onSearchQueryChanged,
                    onNavigateBack = onNavigateBack
                )
            }

            // 2. Блок "Выбранные категории"
            item {
                ShadowCardContainer {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Выбранные категории",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )

                        Spacer(Modifier.height(16.dp))

                        // Список выбранных
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            if (state.selectedCategories.isEmpty()) {
                                Text("Нет выбранных категорий", color = Color.Gray)
                            } else {
                                state.selectedCategories.forEach { category ->
                                    // Оборачиваем в Box для кликабельности (удаление)
                                    Box(modifier = Modifier.clickable { viewModel.onCategoryRemoved(category) }) {
                                        CategoryRowItem(category)
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // Кнопка "Создать категорию" (если нужно, можно оставить)
                        Button(
                            onClick = { viewModel.onAddCategoryClick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF5F5F5),
                                contentColor = SmartBudgetTheme.colors.blue
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text("Создать категорию", fontSize = 16.sp)
                        }
                    }
                }
            }

            // Заголовок для доступных
            if (state.availableCategories.isNotEmpty()) {
                item {
                    Text(
                        text = "Доступные категории",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                    )
                }
            }

            // 3. Список доступных категорий
            items(state.availableCategories) { category ->
                // Оборачиваем в Box для обработки клика (добавление)
                Box(modifier = Modifier.clickable { viewModel.onCategorySelected(category) }) {
                    SingleCategoryCard(category)
                }
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}