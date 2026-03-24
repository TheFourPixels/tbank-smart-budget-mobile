package com.tbank.smartbudget.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.feature.auth.components.AuthSubtitle
import com.tbank.smartbudget.feature.auth.components.AuthTextField
import com.tbank.smartbudget.feature.auth.components.AuthTitle
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun LoginPasswordScreen(
    email: String,
    isUserExisting: Boolean,
    userName: String?,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initAuthData(email, isUserExisting, userName)
    }

    val state by viewModel.uiState.collectAsState()


    val displayUserName = state.userName ?: userName
    val displayIsExisting = if (state.email.isNotEmpty()) state.isUserExisting else isUserExisting

    val title = if (displayIsExisting) {
        "Здравствуйте, ${displayUserName ?: "пользователь"}!"
    } else {
        "Регистрация"
    }

    val subtitle = if (displayIsExisting) {
        "Введите пароль для входа"
    } else {
        "Придумайте пароль для нового аккаунта"
    }

    val buttonText = if (displayIsExisting) "Войти" else "Зарегистрироваться"

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp, vertical = 100.dp),
            verticalArrangement = Arrangement.Top
        ) {
            AuthTitle(title)
            AuthSubtitle(subtitle)

            AuthTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChanged,
                placeholder = "Пароль",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.error ?: "Неизвестная ошибка",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onSubmitPassword {
                        onSuccess()
                    }
                },
                enabled = state.isPasswordValid && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SmartBudgetTheme.colors.yellow,
                    disabledContainerColor = Color(0xFFE0E0E0)
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