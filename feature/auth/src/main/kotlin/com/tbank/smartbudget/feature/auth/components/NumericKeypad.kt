package com.tbank.smartbudget.feature.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
@Composable
fun NumericKeypad(
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val rows = listOf(
            listOf('1', '2', '3'),
            listOf('4', '5', '6'),
            listOf('7', '8', '9'),
            listOf(null, '0', '<') // null - пустое место, < - backspace
        )

        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { char ->
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .clickable(enabled = char != null) {
                                if (char == '<') onBackspaceClick()
                                else if (char != null) onDigitClick(char)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (char != null && char != '<') {
                            Text(
                                text = char.toString(),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 28.sp
                                ),
                                color = Color.Black
                            )
                        } else if (char == '<') {
                            Text(
                                text = "⌫", // Backspace symbol
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}