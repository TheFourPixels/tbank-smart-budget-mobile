package com.tbank.smartbudget.presentation.ui.budget_tab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WhiteBackgroundContainer(content: @Composable () -> Unit) {
    // Используем Box с кастомной тенью
    val shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)

    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Companion.Black.copy(alpha = 0.4f),
                spotColor = Color.Companion.Black.copy(alpha = 0.5f)
            )
            .background(Color.Companion.White, shape = shape)
    ) {
        content()
    }
}