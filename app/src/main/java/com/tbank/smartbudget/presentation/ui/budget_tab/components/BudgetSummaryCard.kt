package com.tbank.smartbudget.presentation.ui.budget_tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tbank.smartbudget.presentation.ui.theme.SmartBudgetTheme

@Composable
fun BudgetSummaryCard(budgetName: String, balance: String, term: String, onClick: () -> Unit) {
    // Используем Box с кастомной тенью
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Companion.Black.copy(alpha = 0.4f),
                spotColor = Color.Companion.Black.copy(alpha = 0.5f)
            )
            .background(
                brush = Brush.Companion.horizontalGradient(
                    colors = listOf(
                        SmartBudgetTheme.colors.gradientDarkBlue,
                        SmartBudgetTheme.colors.gradientGreen
                    )
                ),
                shape = shape
            )
            .clip(shape) // Обрезаем рипл эффект по форме
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Бюджет “$budgetName”",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Companion.Bold),
                color = Color.Companion.White
            )
            Spacer(Modifier.Companion.height(16.dp))
            Row(
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Column {
                    Text(
                        "Баланс",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Companion.White.copy(alpha = 0.7f)
                    )
                    Text(
                        balance,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Companion.ExtraBold),
                        color = Color.Companion.White
                    )
                }
                Spacer(modifier = Modifier.Companion.width(18.dp))

                Column(horizontalAlignment = Alignment.Companion.Start) {
                    Text(
                        "Срок",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Companion.White.copy(alpha = 0.7f)
                    )
                    Text(
                        term,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Companion.ExtraBold),
                        color = Color.Companion.White
                    )
                }
            }
        }
    }
}