package com.tbank.smartbudget.feature.selected_categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.feature.selected_categories.components.*
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.CategoryId

@Composable
fun SelectedCategoriesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: SelectedCategoriesViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SelectedCategoriesEffect.NavigateToCreateCategory -> onNavigateToCreate()
                is SelectedCategoriesEffect.ShowError -> { /* Показать Snackbar или Toast */ }
            }
        }
    }

    SelectedCategoriesContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onSearchQueryChanged = { viewModel.onIntent(SelectedCategoriesIntent.OnSearchQueryChanged(it)) },
        onCategoryRemoved = { viewModel.onIntent(SelectedCategoriesIntent.OnCategoryRemoved(it)) },
        onCategorySelected = { viewModel.onIntent(SelectedCategoriesIntent.OnCategorySelected(it)) },
        onCreateCategoryClick = { viewModel.onIntent(SelectedCategoriesIntent.OnCreateCategoryClick) }
    )
}

@Composable
private fun SelectedCategoriesContent(
    state: SelectedCategoriesUiState,
    onNavigateBack: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCategoryRemoved: (SelectedCategoryUi) -> Unit,
    onCategorySelected: (SelectedCategoryUi) -> Unit,
    onCreateCategoryClick: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF8F8F8)
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(color = SmartBudgetTheme.colors.blue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                    SearchInputBar(
                        searchText = state.searchQuery,
                        onSearchTextChange = onSearchQueryChanged,
                        onNavigateBack = onNavigateBack
                    )
                }

                item {
                    SelectedCategoriesCard(
                        selectedCategories = state.selectedCategories,
                        onCategoryRemoved = onCategoryRemoved,
                        onCreateCategoryClick = onCreateCategoryClick
                    )
                }

                if (state.availableCategories.isNotEmpty()) {
                    item {
                        Text(
                            text = "Доступные категории",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                        )
                    }
                }

                items(state.availableCategories) { category ->
                    Box(modifier = Modifier.clickable { onCategorySelected(category) }) {
                        SingleCategoryCard(category)
                    }
                }

                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectedCategoriesScreenPreview() {
    SmartBudgetTheme {
        SelectedCategoriesContent(
            state = SelectedCategoriesUiState(
                selectedCategories = listOf(
                    SelectedCategoryUi(CategoryId(1), "Еда", "Лимит 15 000 ₽", Color.Red),
                    SelectedCategoryUi(CategoryId(2), "Транспорт", "Лимит 5 000 ₽", Color.Blue)
                ),
                availableCategories = listOf(
                    SelectedCategoryUi(CategoryId(3), "Развлечения", "Без лимита", Color.Magenta),
                    SelectedCategoryUi(CategoryId(4), "Здоровье", "Без лимита", Color.Green)
                )
            ),
            onNavigateBack = {},
            onSearchQueryChanged = {},
            onCategoryRemoved = {},
            onCategorySelected = {},
            onCreateCategoryClick = {}
        )
    }
}
