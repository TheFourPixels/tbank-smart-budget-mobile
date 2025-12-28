package com.tbank.smartbudget.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.presentation.ui.auth.components.AuthSubtitle
import com.tbank.smartbudget.presentation.ui.auth.components.AuthTitle
import com.tbank.smartbudget.presentation.ui.auth.components.NumericKeypad
import com.tbank.smartbudget.presentation.ui.auth.components.PinIndicator

@Composable
fun EnterPinScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Автоматическая отправка, когда набрано 4 цифры
    LaunchedEffect(state.isPinComplete) {
        if (state.isPinComplete) {
            viewModel.onPinComplete {
                onLoginSuccess()
            }
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AuthTitle("Код доступа")
            AuthSubtitle("Придумайте код для быстрого входа")

            Spacer(modifier = Modifier.height(32.dp))

            PinIndicator(filledCount = state.pinCode.length)

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(32.dp))
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.weight(1f))

            // Клавиатура
            NumericKeypad(
                onDigitClick = viewModel::onPinDigitEntered,
                onBackspaceClick = viewModel::onPinBackspace
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}