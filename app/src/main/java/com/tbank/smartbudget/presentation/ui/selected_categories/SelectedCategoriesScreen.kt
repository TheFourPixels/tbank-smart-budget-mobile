package com.tbank.smartbudget.presentation.ui.selected_categories

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
        containerColor = Color(0xFFF8F8F8) // Очень светлый серый фон как на скриншоте
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
                            state.selectedCategories.forEach { category ->
                                CategoryRowItem(category)
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // Кнопка "Добавить категорию"
                        Button(
                            onClick = { viewModel.onAddCategoryClick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF5F5F5), // Светло-серый фон кнопки
                                contentColor = SmartBudgetTheme.colors.blue
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text("Добавить категорию", fontSize = 16.sp)
                        }
                    }
                }
            }

            // 3. Список доступных категорий (карточки ниже)
            items(state.availableCategories) { category ->
                // Используем отдельные карточки для списка внизу, как на макете
                SingleCategoryCard(category)
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}
