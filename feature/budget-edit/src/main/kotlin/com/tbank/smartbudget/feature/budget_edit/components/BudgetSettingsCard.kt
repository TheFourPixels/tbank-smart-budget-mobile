package com.tbank.smartbudget.feature.budget_edit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun BudgetSettingsCard(
    periods: List<String>,
    selectedPeriodIndex: Int,
    onPeriodSelected: (Int) -> Unit,
    amount: String,
    onAmountChanged: (String) -> Unit,
    isPercentMode: Boolean,
    onToggleLimitType: () -> Unit,
    sourceCardName: String,
    sourceCardPan: String,
    sourceCardBalance: Double,
    onChangeSourceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        DetailsCard {
            Text(
                "Настройки бюджета",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                fontSize = 20.sp
            )
            Spacer(Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(periods) { index, text ->
                    PeriodChip(
                        text = text,
                        isSelected = index == selectedPeriodIndex,
                        onClick = { onPeriodSelected(index) }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InputBox(
                    label = "Сумма вклада",
                    value = amount,
                    onValueChange = onAmountChanged,
                    modifier = Modifier.weight(1f)
                )
                UnitSwitchBox(
                    isPercentMode = isPercentMode,
                    onToggle = onToggleLimitType,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Мы берем $amount отсюда",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    "Изменить",
                    style = MaterialTheme.typography.bodySmall,
                    color = SmartBudgetTheme.colors.blue,
                    modifier = Modifier.clickable { onChangeSourceClick() }
                )
            }
            Spacer(Modifier.height(12.dp))
            DarkSourceCard(sourceCardBalance, sourceCardPan, sourceCardName)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetSettingsCardPreview() {
    SmartBudgetTheme {
        BudgetSettingsCard(
            periods = listOf("Месяц", "Квартал", "Год"),
            selectedPeriodIndex = 0,
            onPeriodSelected = {},
            amount = "15 000 ₽",
            onAmountChanged = {},
            isPercentMode = false,
            onToggleLimitType = {},
            sourceCardName = "Black",
            sourceCardPan = "*4455",
            sourceCardBalance = 45000.0,
            onChangeSourceClick = {}
        )
    }
}
