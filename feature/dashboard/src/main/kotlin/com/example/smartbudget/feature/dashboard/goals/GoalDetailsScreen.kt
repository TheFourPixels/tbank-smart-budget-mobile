package com.example.smartbudget.feature.dashboard.goals

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import com.tbank.smartbudget.data.domain.model.Goal
import com.tbank.smartbudget.data.domain.model.GoalContribution
import com.tbank.smartbudget.data.domain.model.GoalId
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailsScreen(
    goalId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToContribute: (Long, Double) -> Unit,
    viewModel: GoalDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(goalId) {
        viewModel.onIntent(GoalDetailsIntent.LoadGoal(goalId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GoalDetailsEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    GoalDetailsContent(
        state = state,
        onBackClick = { viewModel.onIntent(GoalDetailsIntent.OnBackClicked) },
        onContributeClick = { onNavigateToContribute(it.id.value, it.recommendedMonthly) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailsContent(
    state: GoalDetailsUiState,
    onBackClick: () -> Unit,
    onContributeClick: (Goal) -> Unit
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
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            val backgroundColor = MaterialTheme.colorScheme.background
            val gradientHeight = 400.dp

            // Фон градиент (Линейный от фиолетового к розовому)
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
                val goal = state.goal
                if (goal != null) {
                    Text(
                        text = goal.name,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        // 1. Карточка прогресса (Круг)
                        item {
                            DetailsCard {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                                        CircularProgress(
                                            progress = goal.progressPercent / 100f,
                                            modifier = Modifier.size(180.dp)
                                        )
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "${goal.progressPercent}%",
                                                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                                                color = Color.Black
                                            )
                                            val monthLabel = LocalDate.now().format(DateTimeFormatter.ofPattern("LLLL, yyyy", Locale("ru"))).replaceFirstChar { it.uppercase() }
                                            Text(
                                                text = monthLabel,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(24.dp))

                                    GoalDetailRow(Color(0xFFA5D6A7), "Внесено", "${goal.progressPercent}%")
                                    GoalDetailRow(Color(0xFFFFAB91), "Осталось", "${100 - goal.progressPercent}%")
                                    GoalDetailRow(Color(0xFFB39DDB), "Дедлайн", goal.deadline ?: "Не задан")
                                    GoalDetailRow(Color(0xFFE0E0E0), "До дедлайна", "${goal.daysLeft} дней")
                                }
                            }
                        }

                        // 2. История пополнений
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "История пополнений",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    "+${formatMoney(goal.savedAmount)} ₽",
                                    color = Color.LightGray,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        if (goal.contributions.isEmpty()) {
                            item {
                                Text("Пополнений пока не было", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                            }
                        } else {
                            items(goal.contributions) { contribution ->
                                ContributionItem(contribution)
                            }
                        }

                        // Кнопки действий
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = { onContributeClick(goal) },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600), contentColor = Color.Black),
                                    shape = RoundedCornerShape(16.dp),
                                    enabled = !state.isContributing
                                ) {
                                    if (state.isContributing) {
                                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                                    } else {
                                        Text("Пополнить", fontWeight = FontWeight.Bold)
                                    }
                                }
                                
                                TextButton(
                                    onClick = { /* TODO */ },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Завершить цель досрочно", color = Color(0xFF528ECE))
                                }
                            }
                        }
                    }
                } else if (state.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.error, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun CircularProgress(progress: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = 20.dp.toPx()
        // Фон круга
        drawArc(
            color = Color(0xFFEDE7F6),
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            size = Size(size.width, size.height)
        )
        // Прогресс
        drawArc(
            color = Color(0xFF7E57C2),
            startAngle = 135f,
            sweepAngle = 270f * progress,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            size = Size(size.width, size.height)
        )
    }
}

@Composable
fun GoalDetailRow(color: Color, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(12.dp))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
    }
}

@Composable
fun ContributionItem(contribution: GoalContribution) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFEEEEEE)))
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                contribution.description ?: "Пополнение",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                contribution.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            "+${formatMoney(contribution.amount)} ₽",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoalDetailsScreenPreview() {
    SmartBudgetTheme {
        GoalDetailsContent(
            state = GoalDetailsUiState(
                goal = Goal(
                    id = GoalId(1),
                    name = "Накопить на авто",
                    targetAmount = 150000.0,
                    savedAmount = 45000.0,
                    deadline = "05.04.2026",
                    progressPercent = 78,
                    daysLeft = 25,
                    recommendedMonthly = 2300.0,
                    contributions = listOf(
                        GoalContribution(50.0, LocalDateTime.now(), "Пополнение"),
                        GoalContribution(100.0, LocalDateTime.now().minusDays(5), "Пополнение"),
                        GoalContribution(100.0, LocalDateTime.now().minusDays(30), "Первый взнос")
                    )
                )
            ),
            onBackClick = {},
            onContributeClick = {}
        )
    }
}

private fun formatMoney(amount: Double): String = "%,.0f".format(amount).replace(',', ' ')
