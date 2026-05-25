package com.example.smartbudget.feature.category_search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartbudget.feature.category_search.components.SearchAppBar
import com.example.smartbudget.feature.category_search.components.SearchSection
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.CategoryId

@Composable
fun CategorySearchScreen(
    onNavigateBack: () -> Unit,
    onCategoryClick: (String) -> Unit,
    viewModel: CategorySearchViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CategorySearchEffect.NavigateBackWithResult -> onCategoryClick(effect.categoryName)
                CategorySearchEffect.Exit -> onNavigateBack()
            }
        }
    }

    CategorySearchContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onQueryChanged = { viewModel.onIntent(CategorySearchIntent.OnQueryChanged(it)) },
        onCategorySelected = { viewModel.onIntent(CategorySearchIntent.OnCategorySelected(it)) },
        focusRequester = focusRequester
    )
}

@Composable
private fun CategorySearchContent(
    state: CategorySearchUiState,
    onNavigateBack: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val topResult = state.searchResults.firstOrNull { it.isTopResult }
    val otherCategories = remember(state.searchResults, topResult) {
        if (topResult != null) state.searchResults.filter { !it.isTopResult }
        else state.searchResults
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            SearchAppBar(
                searchText = state.searchQuery,
                onSearchTextChange = onQueryChanged,
                onCancelClick = onNavigateBack,
                focusRequester = focusRequester
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (topResult != null) {
                    item {
                        SearchSection(
                            title = "Самое подходящее",
                            items = listOf(topResult),
                            onCategoryClick = onCategorySelected
                        )
                    }
                }

                if (otherCategories.isNotEmpty()) {
                    item {
                        SearchSection(
                            title = if (topResult != null) "Похожие категории" else "Все категории",
                            items = otherCategories,
                            onCategoryClick = onCategorySelected
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategorySearchScreenPreview() {
    SmartBudgetTheme {
        CategorySearchContent(
            state = CategorySearchUiState(
                searchQuery = "",
                searchResults = listOf(
                    SearchCategoryItem(
                        id = CategoryId(1),
                        name = "Продукты",
                        iconRes = 0,
                        color = Color.Green,
                        limit = "Остаток: 10 000 ₽",
                        isTopResult = true
                    ),
                    SearchCategoryItem(
                        id = CategoryId(2),
                        name = "Транспорт",
                        iconRes = 0,
                        color = Color.Blue,
                        limit = "Остаток: 5 000 ₽",
                        isTopResult = false
                    )
                )
            ),
            onNavigateBack = {},
            onQueryChanged = {},
            onCategorySelected = {},
            focusRequester = remember { FocusRequester() }
        )
    }
}
