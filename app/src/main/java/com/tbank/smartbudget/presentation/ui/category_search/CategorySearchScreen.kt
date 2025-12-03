package com.tbank.smartbudget.presentation.ui.category_search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.domain.model.SearchCategoryItem
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun CategorySearchScreen(
    onNavigateBack: () -> Unit,
    viewModel: CategorySearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // 1. Создаем объект FocusRequester
    val focusRequester = remember { FocusRequester() }

    // Логика разделения списка
    val topResult = state.searchResults.firstOrNull { it.isTopResult }

    val otherCategories = remember(state.searchResults, topResult) {
        if (topResult != null) {
            state.searchResults.filter { !it.isTopResult }
        } else {
            state.searchResults
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // 2. Передаем FocusRequester в SearchAppBar
            SearchAppBar(
                searchText = state.searchQuery,
                onSearchTextChange = viewModel::onSearchQueryChanged,
                onCancelClick = onNavigateBack,
                focusRequester = focusRequester // Передача FocusRequester
            )

            // 3. Запрашиваем фокус сразу после входа на экран
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            // 4. Список результатов
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Секция "Самое подходящее"
                if (topResult != null) {
                    item {
                        Text(
                            "Самое подходящее",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 72.dp)
                                .shadow(
                                    elevation = 10.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.4f),
                                    spotColor = Color.Black.copy(alpha = 0.5f)
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.CenterStart
                        ){
                            CategoryItemRow(
                                category = topResult,
                                onClick = { /* TODO: Обработка выбора */ }
                            )
                        }
                    }
                }

                // Секция "Все категории"
                if (otherCategories.isNotEmpty()) {
                    item {
                        Text(
                            if (topResult != null) "Похожие категории" else "Все категории",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 10.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.4f),
                                    spotColor = Color.Black.copy(alpha = 0.5f)
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                otherCategories.forEach { category ->
                                    CategoryItemRow(
                                        category = category,
                                        onClick = { /* TODO: Обработка выбора */ }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- UI Компоненты ---

@Composable
fun SearchAppBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    focusRequester: FocusRequester // НОВЫЙ ПАРАМЕТР
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchInput(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onClearClick = { onSearchTextChange("") },
            modifier = Modifier.weight(1f),
            focusRequester = focusRequester // Передаем FocusRequester дальше
        )

        TextButton(onClick = onCancelClick) {
            Text(
                "Отменить",
                color = SmartBudgetTheme.colors.blue,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun SearchInput(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester
) {
    val textStyle = TextStyle(color = Color.Black, fontSize = 15.sp)
    val backgroundColor = Color(0xFFE0E0E0)

    Box(
        modifier = modifier
            .padding(start = 16.dp)
            .height(35.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Поиск",
                tint = Color.DarkGray.copy(alpha = 0.6f),
                modifier = Modifier.size(30.dp).padding(end = 8.dp)
            )

            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                singleLine = true,
                textStyle = textStyle,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester), // *** Применяем FocusRequester к полю ввода ***
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

            if (searchText.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = "Очистить",
                    tint = Color.DarkGray.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onClearClick)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryItemRow(category: SearchCategoryItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryIconPlaceholder(category.color)
        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                category.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
            )

            if (!category.isTopResult && category.limit.isNotEmpty()) {
                Text(
                    category.limit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun CategoryIconPlaceholder(color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Text("🛍️", fontSize = 20.sp)
    }
}