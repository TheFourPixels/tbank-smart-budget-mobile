package com.example.smartbudget.feature.dashboard.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.Goal
import com.tbank.smartbudget.data.domain.model.GoalId

@Composable
fun GoalsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddGoal: () -> Unit,
    onNavigateToGoalDetails: (Long) -> Unit,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GoalsEffect.NavigateBack -> onNavigateBack()
                GoalsEffect.NavigateToAddGoal -> onNavigateToAddGoal()
            }
        }
    }

    GoalsContent(
        state = state,
        onBackClick = { viewModel.onIntent(GoalsIntent.OnBackClick) },
        onAddGoalClick = { viewModel.onIntent(GoalsIntent.OnAddGoalClick) },
        onGoalClick = { onNavigateToGoalDetails(it.id.value) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsContent(
    state: GoalsUiState,
    onBackClick: () -> Unit,
    onAddGoalClick: () -> Unit,
    onGoalClick: (Goal) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddGoalClick,
                containerColor = Color(0xFFFFD600),
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, "Добавить цель")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            val backgroundColor = MaterialTheme.colorScheme.background
            val gradientHeight = 400.dp
            
            // Основной градиент
            Box(modifier = Modifier.fillMaxWidth().height(gradientHeight).background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF5E35B1), Color(0xFFD81B60).copy(alpha = 0.7f)),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, 600f)
                )
            ))
            
            // Плавный переход в фон снизу
            Box(modifier = Modifier.fillMaxWidth().height(gradientHeight).background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, backgroundColor.copy(alpha = 0f), backgroundColor),
                    startY = 0.6f * with(androidx.compose.ui.platform.LocalDensity.current) { gradientHeight.toPx() },
                    endY = 1.0f * with(androidx.compose.ui.platform.LocalDensity.current) { gradientHeight.toPx() }
                )
            ))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Мои цели",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (state.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.error, color = MaterialTheme.colorScheme.error)
                    }
                } else if (state.goals.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("У вас пока нет активных целей", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(state.goals) { goal ->
                            GoalCard(
                                goal = goal,
                                onClick = { onGoalClick(goal) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoalCard(goal: Goal, onClick: () -> Unit) {
    DetailsCard(modifier = Modifier.clickable { onClick() }) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = goal.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            
            Spacer(Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildString {
                        append(formatMoney(goal.savedAmount))
                        append(" ₽ из ")
                        append(formatMoney(goal.targetAmount))
                        append(" ₽")
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${goal.progressPercent} %",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { goal.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = Color(0xFFF06292),
                trackColor = Color(0xFFF8BBD0)
            )

            Spacer(Modifier.height(24.dp))

            GoalInfoRow(
                iconColor = Color(0xFF42A5F5),
                text = "Осталось: ${goal.daysLeft} дней"
            )
            
            Spacer(Modifier.height(12.dp))

            GoalInfoRow(
                iconColor = Color(0xFF42A5F5),
                text = "Рекомендуем: ${formatMoney(goal.recommendedMonthly)} ₽ / мес"
            )
        }
    }
}

@Composable
fun GoalInfoRow(iconColor: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(iconColor)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF37474F)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoalsScreenPreview() {
    SmartBudgetTheme {
        GoalsContent(
            state = GoalsUiState(
                goals = listOf(
                    Goal(GoalId(1), "Накопить на авто", 150000.0, 45000.0, "05.04.2026", 30, 45, 2300.0),
                    Goal(GoalId(2), "Отпуск", 50000.0, 25000.0, "15.08.2026", 50, 120, 5000.0)
                )
            ),
            onBackClick = {},
            onAddGoalClick = {},
            onGoalClick = {}
        )
    }
}

private fun formatMoney(amount: Double): String = "%,.0f".format(amount).replace(',', ' ')
