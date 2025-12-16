package com.tbank.smartbudget.presentation.ui.budget_edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DarkSourceCard(amount: String, cardNumber: String, cardName: String) {
    Card(
        modifier = Modifier.Companion.fillMaxWidth().height(140.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C)) // Почти черный
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = amount,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Companion.Medium),
                color = Color.Companion.White
            )

            Column {
                Text(
                    text = cardNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Companion.White
                )
                Text(
                    text = cardName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Companion.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}