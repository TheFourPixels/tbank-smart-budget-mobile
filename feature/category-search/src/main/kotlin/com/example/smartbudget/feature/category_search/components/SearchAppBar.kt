package com.example.smartbudget.feature.category_search.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun SearchAppBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCancelClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Назад",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        SearchInput(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onClearClick = { onSearchTextChange("") },
            modifier = Modifier.weight(1f),
            focusRequester = focusRequester
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
