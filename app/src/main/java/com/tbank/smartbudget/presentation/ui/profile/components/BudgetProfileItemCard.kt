package com.tbank.smartbudget.presentation.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.smartbudget.presentation.ui.profile.BudgetProfileItem

@Composable
fun BudgetProfileItemCard(item: BudgetProfileItem) {
    Box(
        modifier = Modifier.Companion
            .width(140.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.Companion.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.Companion
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(item.color),
                contentAlignment = Alignment.Companion.Center
            ) {
                Text(
                    text = item.initial,
                    color = Color.Companion.White,
                    fontWeight = FontWeight.Companion.Bold,
                    fontSize = 14.sp
                )
            }

            Column {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Companion.Bold),
                    color = Color.Companion.Black,
                    maxLines = 1
                )
                Text(
                    text = item.dateDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Companion.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}