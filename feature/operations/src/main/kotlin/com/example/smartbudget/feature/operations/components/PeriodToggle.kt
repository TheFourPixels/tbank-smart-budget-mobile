package com.example.smartbudget.feature.operations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartbudget.feature.operations.PeriodType

@Composable
fun PeriodToggle(selectedType: PeriodType, onTypeSelected: (PeriodType) -> Unit) {
    Box(
        modifier = Modifier.background(Color(0xFFF5F5F5), RoundedCornerShape(50))
            .padding(4.dp)
    ) {
        Row {
            listOf(PeriodType.WEEK to "Нед", PeriodType.MONTH to "Мес").forEach { (type, label) ->
                Box(
                    modifier = if (selectedType == type) Modifier.weight(1f)
                        .shadow(1.dp, androidx.compose.foundation.shape.RoundedCornerShape(50))
                        .background(
                            Color.White,
                            androidx.compose.foundation.shape.RoundedCornerShape(50)
                        ).clickable { onTypeSelected(type) }.padding(vertical = 8.dp)
                    else Modifier.weight(1f)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                        .clickable { onTypeSelected(type) }.padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }
    }
}