package com.tbank.smartbudget.presentation.ui.selected_categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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

// --- UI КОМПОНЕНТЫ ---

@Composable
fun SearchInputBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Используем стиль как в BudgetTab, но без стрелки "Назад" внутри поля
    // Если нужна кнопка назад снаружи, раскомментируйте IconButton ниже
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Кнопка Назад (опционально, если это не корневой экран)
        /*IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.Black)
        }
        Spacer(Modifier.width(8.dp))*/

        Box(
            modifier = Modifier
                .height(44.dp)
                .weight(1f)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchText.isEmpty()) {
                                Text("Поиск", color = Color.Gray, fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    }
                )
                if (searchText.isNotEmpty()) {
                    Icon(
                        Icons.Filled.Cancel,
                        contentDescription = "Очистить",
                        tint = Color.Gray,
                        modifier = Modifier.clickable { onSearchTextChange("") }
                    )
                }
            }
        }
    }
}

@Composable
fun ShadowCardContainer(content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(24.dp) // Более округлые углы как на макете
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.4f),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .background(Color.White, shape)
    ) {
        content()
    }
}

@Composable
fun CategoryRowItem(category: SelectedCategoryUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(category.color),
            contentAlignment = Alignment.Center
        ) {
            Text("🛍️", fontSize = 20.sp) // Заглушка
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = Color.Black
            )
            Text(
                text = category.limitDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SingleCategoryCard(category: SelectedCategoryUi) {
    ShadowCardContainer {
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            CategoryRowItem(category)
        }
    }
}