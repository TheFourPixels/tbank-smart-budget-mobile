package com.tbank.smartbudget.feature.budget_details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun SettingsCard(
    isLimitNotificationEnabled: Boolean,
    onLimitNotificationToggle: (Boolean) -> Unit,
    isOperationNotificationEnabled: Boolean,
    onOperationNotificationToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        DetailsCard {
            Text(
                "Настройки",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingSwitchRow(
                title = "Оповещение\nо превышении лимита",
                checked = isLimitNotificationEnabled,
                onCheckedChange = onLimitNotificationToggle
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingSwitchRow(
                title = "Уведомления\nоб операциях",
                checked = isOperationNotificationEnabled,
                onCheckedChange = onOperationNotificationToggle
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsCardPreview() {
    SmartBudgetTheme {
        SettingsCard(
            isLimitNotificationEnabled = true,
            onLimitNotificationToggle = {},
            isOperationNotificationEnabled = false,
            onOperationNotificationToggle = {}
        )
    }
}
