package com.tbank.smartbudget.feature.budget_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun BudgetHeader(
    budgetName: String,
    currentBalance: String,
    periodDescription: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 18.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(start = 23.dp),
            text = "Бюджет “$budgetName”",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W700),
            color = Color.White,
            lineHeight = 23.sp
        )
        Text(
            modifier = Modifier.padding(start = 23.dp),
            text = currentBalance,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp),
            color = Color.White,
            lineHeight = 33.sp
        )
        Text(
            modifier = Modifier.padding(start = 23.dp),
            text = periodDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f),
            lineHeight = 23.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onEditClick,
            modifier = Modifier
                .width(320.dp)
                .height(50.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.2f),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Редактировать", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2196F3)
@Composable
private fun BudgetHeaderPreview() {
    SmartBudgetTheme {
        BudgetHeader(
            budgetName = "Кубышка",
            currentBalance = "13 900 ₽",
            periodDescription = "На 2 месяца",
            onEditClick = {}
        )
    }
}
