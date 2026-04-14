package com.tbank.smartbudget.feature.budget_edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.tbank.smartbudget.feature.budget_edit.EditCategoryUi

@Composable
fun BudgetCategoriesCard(
    categories: List<EditCategoryUi>,
    onAddCategoryClick: () -> Unit,
    onCategoryLimitChanged: (Long, String) -> Unit,
    onCategoryTypeToggle: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        DetailsCard {
            Text(
                "Категории",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                fontSize = 20.sp
            )
            Spacer(Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                categories.forEach { category ->
                    EditCategoryRow(
                        category = category,
                        onLimitChange = { onCategoryLimitChanged(category.id.value, it) },
                        onTypeToggle = { onCategoryTypeToggle(category.id.value) }
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onAddCategoryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F5F5),
                    contentColor = SmartBudgetTheme.colors.blue
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Добавить категорию", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetCategoriesCardPreview() {
    SmartBudgetTheme {
        BudgetCategoriesCard(
            categories = emptyList(),
            onAddCategoryClick = {},
            onCategoryLimitChanged = { _, _ -> },
            onCategoryTypeToggle = {}
        )
    }
}
