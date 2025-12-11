package com.tbank.smartbudget.presentation.ui.budget_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun SettingSwitchRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Text(
            title, style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF424242)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Companion.White,
                checkedTrackColor = SmartBudgetTheme.colors.blue,
                uncheckedThumbColor = Color.Companion.White,
                uncheckedTrackColor = Color.Companion.LightGray
            )
        )
    }
}