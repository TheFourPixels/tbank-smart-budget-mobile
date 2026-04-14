package com.example.smartbudget.feature.category_search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartbudget.feature.category_search.SearchCategoryItem
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.CategoryId

@Composable
fun SearchSection(
    title: String,
    items: List<SearchCategoryItem>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            Column {
                items.forEach { category ->
                    CategoryItemRow(
                        category = category,
                        onClick = { onCategoryClick(category.name) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSectionPreview() {
    SmartBudgetTheme {
        SearchSection(
            title = "Все категории",
            items = listOf(
                SearchCategoryItem(
                    id = CategoryId(1),
                    name = "Еда",
                    iconRes = 0,
                    color = Color.Red,
                    limit = "1000",
                    isTopResult = true
                ),
                SearchCategoryItem(
                    id = CategoryId(2),
                    name = "Транспорт",
                    iconRes = 0,
                    color = Color.Blue,
                    limit = "500",
                    isTopResult = false
                )
            ),
            onCategoryClick = {}
        )
    }
}
