package com.tbank.smartbudget.feature.selected_categories.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.CategoryId
import com.tbank.smartbudget.feature.selected_categories.SelectedCategoryUi

@Composable
fun SelectedCategoriesCard(
    selectedCategories: List<SelectedCategoryUi>,
    onCategoryRemoved: (SelectedCategoryUi) -> Unit,
    onCreateCategoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ShadowCardContainer {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Выбранные категории",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (selectedCategories.isEmpty()) {
                        Text("Нет выбранных категорий", color = Color.Gray)
                    } else {
                        selectedCategories.forEach { category ->
                            Box(modifier = Modifier.clickable { onCategoryRemoved(category) }) {
                                CategoryRowItem(category)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onCreateCategoryClick,
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
                    Text("Создать категорию", fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectedCategoriesCardPreview() {
    SmartBudgetTheme {
        SelectedCategoriesCard(
            selectedCategories = listOf(
                SelectedCategoryUi(CategoryId(1), "Еда", "Лимит 15 000 ₽", Color.Red),
                SelectedCategoryUi(CategoryId(2), "Транспорт", "Лимит 5 000 ₽", Color.Blue)
            ),
            onCategoryRemoved = {},
            onCreateCategoryClick = {}
        )
    }
}
