package com.tbank.smartbudget.presentation.ui.category_search.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun SearchAppBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    focusRequester: FocusRequester // НОВЫЙ ПАРАМЕТР
) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        SearchInput(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onClearClick = { onSearchTextChange("") },
            modifier = Modifier.Companion.weight(1f),
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