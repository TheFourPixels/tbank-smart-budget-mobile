package com.tbank.smartbudget.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.feature.auth.components.AuthSubtitle
import com.tbank.smartbudget.feature.auth.components.AuthTextField
import com.tbank.smartbudget.feature.auth.components.AuthTitle

@Composable
fun LoginEmailScreen(
    onNavigateNext: (email: String, isExisting: Boolean, userName: String?) -> Unit,
    viewModel: AuthViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AuthEffect.NavigateNext -> {
                    onNavigateNext(
                        state.email,
                        state.isUserExisting,
                        state.userName
                    )
                }

                is AuthEffect.ShowError -> {

                }
            }
        }
    }

    LoginEmailContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun LoginEmailContent(
    state: AuthUiState,
    onIntent: (AuthIntent) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Spacer(modifier = Modifier.height(88.dp))

            AuthTitle("Вход")
            AuthSubtitle("Введите вашу почту для входа или регистрации")

            AuthTextField(
                value = state.email,
                onValueChange = {
                    onIntent(AuthIntent.OnEmailChanged(it))
                },
                placeholder = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onIntent(AuthIntent.OnEmailSubmit) },
                enabled = state.isEmailValid && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SmartBudgetTheme.colors.yellow,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text("Далее", fontSize = 16.sp, color = Color.Black)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginEmailPreview() {
    SmartBudgetTheme {
        LoginEmailContent(
            state = AuthUiState(email = "test@example.com", isEmailValid = true),
            onIntent = {}
        )
    }
}
