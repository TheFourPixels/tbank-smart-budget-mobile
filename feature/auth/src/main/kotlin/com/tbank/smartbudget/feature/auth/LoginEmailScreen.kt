package com.tbank.smartbudget.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tbank.smartbudget.feature.auth.components.AuthSubtitle
import com.tbank.smartbudget.feature.auth.components.AuthTextField
import com.tbank.smartbudget.feature.auth.components.AuthTitle
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun LoginEmailScreen(
    onNavigateNext: (email: String, isExisting: Boolean, userName: String?) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

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
            AuthTitle("Вход")
            AuthSubtitle("Введите вашу почту для входа или регистрации")

            AuthTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChanged,
                placeholder = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onEmailSubmit {
                        val currentState = viewModel.uiState.value
                        onNavigateNext(
                            currentState.email,
                            currentState.isUserExisting,
                            currentState.userName
                        )
                    }
                },
                enabled = state.isEmailValid && !state.isLoading,
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
                    Text("Далее", fontSize = 16.sp, color = Color.Black)
                }
            }
        }
    }
}