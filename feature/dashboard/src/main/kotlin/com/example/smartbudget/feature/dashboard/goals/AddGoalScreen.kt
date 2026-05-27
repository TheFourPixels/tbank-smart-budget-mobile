package com.example.smartbudget.feature.dashboard.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tbank.smartbudget.core.ui.common.DetailsCard
import com.tbank.smartbudget.core.ui.theme.SmartBudgetTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AddGoalScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddGoalViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddGoalEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    AddGoalContent(
        state = state,
        onNameChanged = { viewModel.onIntent(AddGoalIntent.OnNameChanged(it)) },
        onAmountChanged = { viewModel.onIntent(AddGoalIntent.OnAmountChanged(it)) },
        onDeadlineChanged = { viewModel.onIntent(AddGoalIntent.OnDeadlineChanged(it)) },
        onSaveClick = { viewModel.onIntent(AddGoalIntent.OnSaveClicked) },
        onBackClick = { viewModel.onIntent(AddGoalIntent.OnBackClicked) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalContent(
    state: AddGoalUiState,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onDeadlineChanged: (LocalDate) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showAmountDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.deadline.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        onDeadlineChanged(date)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showAmountDialog) {
        var tempAmount by remember { mutableStateOf(state.amount) }
        AlertDialog(
            onDismissRequest = { showAmountDialog = false },
            title = { Text("Введите сумму") },
            text = {
                TextField(
                    value = tempAmount,
                    onValueChange = { if (it.all { char -> char.isDigit() }) tempAmount = it },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onAmountChanged(tempAmount)
                    showAmountDialog = false
                }) { Text("OK") }
            }
        )
    }

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
                Spacer(Modifier.height(16.dp))
                
                // Ввод названия
                BasicTextField(
                    value = state.name,
                    onValueChange = onNameChanged,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (state.name.isEmpty()) {
                            Text("Введите название\nцели", color = Color.White.copy(alpha = 0.6f), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        }
                        innerTextField()
                    }
                )

                Spacer(Modifier.height(32.dp))

                DetailsCard {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            "Настройки цели",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        
                        Spacer(Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Сумма
                            GoalInputBox(
                                label = "Сумма",
                                value = "${formatMoney(state.amount.toDoubleOrNull() ?: 0.0)} ₽",
                                onClick = { showAmountDialog = true },
                                modifier = Modifier.weight(1f)
                            )
                            
                            // Дедлайн
                            GoalInputBox(
                                label = "Дедлайн",
                                value = state.deadline.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                                onClick = { showDatePicker = true },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // Рекомендация
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF5F5F5))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "${formatMoney(state.recommendedMonthly)} ₽",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Именно такую сумму мы советуем откладывать каждый месяц, чтобы успеть до дедлайна",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = onSaveClick,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
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
                                Text("Создать цель", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
    }
}

@Composable
fun GoalInputBox(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE1BEE7).copy(alpha = 0.5f)) // Светло-фиолетовый
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddGoalScreenPreview() {
    SmartBudgetTheme {
        AddGoalContent(
            state = AddGoalUiState(
                name = "Накопить на машину",
                amount = "1500000",
                recommendedMonthly = 25000.0
            ),
            onNameChanged = {},
            onAmountChanged = {},
            onDeadlineChanged = {},
            onSaveClick = {},
            onBackClick = {}
        )
    }
}

private fun formatMoney(amount: Double): String = "%,.0f".format(amount).replace(',', ' ')
