package com.example.smartbudget.feature.category_search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartbudget.feature.category_search.SearchCategoryItem
import com.tbank.smartbudget.core.ui.common.CategoryIconPlaceholder

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
        CategoryIconPlaceholder(
            color = category.color,
            iconRes = category.iconRes,
            name = category.name
        )
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