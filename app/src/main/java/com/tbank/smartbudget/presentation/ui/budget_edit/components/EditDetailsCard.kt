package com.tbank.smartbudget.presentation.ui.budget_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditDetailsCard(content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Companion.Black.copy(alpha = 0.4f),
                spotColor = Color.Companion.Black.copy(alpha = 0.5f)
            )
            .background(Color.Companion.White, shape)
    ) {
        Column(
            modifier = Modifier.Companion.padding(24.dp),
            content = content
        )
    }
}