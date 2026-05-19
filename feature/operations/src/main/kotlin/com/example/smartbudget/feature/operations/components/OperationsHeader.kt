package com.example.smartbudget.feature.operations.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.core.ui.common.BasicSearchBar
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun OperationsHeader(
    onBackClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                "Назад",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(onClick = onCalendarClick) {
            Icon(Icons.Default.DateRange, "Календарь", tint = SmartBudgetTheme.colors.blue)
        }

        Spacer(Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            BasicSearchBar(
                searchText = "",
                onSearchTextChange = { },
                modifier = Modifier.fillMaxWidth().clickable { onSearchClick() },
                backgroundColor = SmartBudgetTheme.colors.lightGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OperationsHeaderPreview() {
    SmartBudgetTheme {
        OperationsHeader(
            onBackClick = {},
            onCalendarClick = {},
            onSearchClick = {}
        )
    }
}
