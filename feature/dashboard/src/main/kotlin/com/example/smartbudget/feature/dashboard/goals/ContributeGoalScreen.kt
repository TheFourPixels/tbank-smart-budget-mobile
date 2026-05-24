package com.example.smartbudget.feature.dashboard.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.common.DarkSourceCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme

@Composable
fun ContributeGoalScreen(
    goalId: Long,
    recommendedAmount: Double,
    onNavigateBack: () -> Unit,
    viewModel: ContributeGoalViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(goalId, recommendedAmount) {
        viewModel.onIntent(ContributeGoalIntent.Init(goalId, recommendedAmount))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ContributeGoalEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    ContributeGoalContent(
        state = state,
        onBackClick = { viewModel.onIntent(ContributeGoalIntent.OnBackClicked) },
        onContributeClick = { viewModel.onIntent(ContributeGoalIntent.OnContributeClicked) }
    )
}

@Composable
fun ContributeGoalContent(
    state: ContributeGoalUiState,
    onBackClick: () -> Unit,
    onContributeClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)), // Затемнение фона
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Пополнение цели",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                Spacer(Modifier.height(24.dp))

                // Сумма пополнения
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Сумма пополнения", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        Text(
                            text = "${formatMoney(state.amount.toDoubleOrNull() ?: 0.0)} ₽",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Мы берем ${formatMoney(state.amount.toDoubleOrNull() ?: 0.0)} отсюда",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    TextButton(onClick = { /* TODO: Change source */ }) {
                        Text("Изменить", color = Color(0xFF528ECE))
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Карточка счета (Черная)
                DarkSourceCard(
                    amount = state.cardBalance,
                    cardNumber = state.cardNumber,
                    cardName = state.cardName
                )

                Spacer(Modifier.height(32.dp))

                // Кнопка Пополнить
                Button(
                    onClick = onContributeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Пополнить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                if (state.error != null) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContributeGoalScreenPreview() {
    SmartBudgetTheme {
        ContributeGoalContent(
            state = ContributeGoalUiState(
                amount = "13900",
                cardBalance = 1000.0,
                cardNumber = "• 8563",
                cardName = "Дебетовая карта"
            ),
            onBackClick = {},
            onContributeClick = {}
        )
    }
}

private fun formatMoney(amount: Double): String = "%,.0f".format(amount).replace(',', ' ')
