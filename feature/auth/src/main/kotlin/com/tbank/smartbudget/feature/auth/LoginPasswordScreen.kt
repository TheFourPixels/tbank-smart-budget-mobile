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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.feature.auth.components.AuthSubtitle
import com.tbank.smartbudget.feature.auth.components.AuthTextField
import com.tbank.smartbudget.feature.auth.components.AuthTitle

@Composable
fun LoginPasswordScreen(
    email: String,
    isUserExisting: Boolean,
    userName: String?,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(AuthIntent.InitAuthData(email, isUserExisting, userName))
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect == AuthEffect.NavigateNext) {
                onSuccess()
            }
        }
    }

    LoginPasswordContent(
        state = state,
        onIntent = viewModel::onIntent,
        onBack = onBack
    )
}

@Composable
fun LoginPasswordContent(
    state: AuthUiState,
    onIntent: (AuthIntent) -> Unit,
    onBack: () -> Unit
) {



    val title = if (state.isUserExisting) {
        "Здравствуйте, ${state.userName ?: "пользователь"}!"
    } else {
        "Регистрация"
    }

    val subtitle = if (state.isUserExisting) {
        "Введите пароль для входа"
    } else {
        "Придумайте пароль для нового аккаунта"
    }

    val buttonText = if (state.isUserExisting) "Войти" else "Зарегистрироваться"

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

            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            AuthTitle(title)
            AuthSubtitle(subtitle)

            AuthTextField(
                value = state.password,
                onValueChange = { onIntent(AuthIntent.OnPasswordChanged(it)) },
                placeholder = "Пароль",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onIntent(AuthIntent.OnPasswordSubmit)
                },
                enabled = state.isPasswordValid && !state.isLoading,
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
                    Text(buttonText, fontSize = 16.sp, color = Color.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Login Mode")
@Composable
private fun LoginPasswordPreview() {
    SmartBudgetTheme {
        LoginPasswordContent(
            state = AuthUiState(
                userName = "Александр",
                isUserExisting = true,
                password = "password123",
                isPasswordValid = true
            ),
            onIntent = {},
            onBack = {}
        )
    }
}
