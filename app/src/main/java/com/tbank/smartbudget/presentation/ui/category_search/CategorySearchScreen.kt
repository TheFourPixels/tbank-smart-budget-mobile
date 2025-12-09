package com.tbank.smartbudget.presentation.ui.category_search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.category_search.components.CategoryItemRow
import com.tbank.smartbudget.presentation.ui.category_search.components.SearchAppBar

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

