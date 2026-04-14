package com.tbank.smartbudget.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.feature.auth.components.AuthSubtitle
import com.tbank.smartbudget.feature.auth.components.AuthTitle
import com.tbank.smartbudget.feature.auth.components.NumericKeypad
import com.tbank.smartbudget.feature.auth.components.PinIndicator

@Composable
fun EnterPinScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is AuthEffect.NavigateNext) {
                onLoginSuccess()
            }
        }
    }

    EnterPinContent(
        state = state,
        onIntent = { intent -> viewModel.onIntent(intent) }
    )
}

@Composable
fun EnterPinContent(
    state: AuthUiState,
    onIntent: (AuthIntent) -> Unit
) {
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

            Box(modifier = Modifier.height(60.dp), contentAlignment = Alignment.Center) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = SmartBudgetTheme.colors.blue)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            NumericKeypad(
                onDigitClick = { digit ->
                    onIntent(AuthIntent.OnPinDigitEntered(digit))
                },
                onBackspaceClick = {
                    onIntent(AuthIntent.OnPinBackspace)
                }
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnterPinPreview() {
    SmartBudgetTheme {
        EnterPinContent(
            state = AuthUiState(
                pinCode = "12",
                isLoading = false
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun EnterPinLoadingPreview() {
    SmartBudgetTheme {
        EnterPinContent(
            state = AuthUiState(
                pinCode = "1234",
                isLoading = true
            ),
            onIntent = {}
        )
    }
}