package com.tbank.smartbudget.presentation.ui.all_operations

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.model.Transaction
import com.tbank.smartbudget.domain.model.TransactionType
import com.tbank.smartbudget.domain.usecase.GetTransactionsUseCase
import com.tbank.smartbudget.domain.usecase.TransactionsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AllOperationsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllOperationsUiState(isLoading = true))
    val uiState: StateFlow<AllOperationsUiState> = _uiState.asStateFlow()

    private var cachedResult: TransactionsResult? = null
    private var currentQuery = ""
    private var currentDateStart: LocalDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
    private var currentDateEnd: LocalDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())

    init {
        setMonthPeriod()
    }

    fun onSearchQueryChanged(query: String) {
        currentQuery = query
        loadData()
    }

    // Метод, вызываемый при возврате с экрана поиска с результатом
    fun onCategorySearchResult(categoryName: String) {
        // Добавляем категорию в выбранные, если её там нет
        val currentSelection = _uiState.value.selectedCategoryNames
        if (!currentSelection.contains(categoryName)) {
            val newSelection = currentSelection + categoryName
            _uiState.update { it.copy(selectedCategoryNames = newSelection) }
            applyLocalFilters()
        }
    }

    fun onPeriodChanged(periodType: PeriodType) {
        when (periodType) {
            PeriodType.WEEK -> setWeekPeriod()
            PeriodType.MONTH -> setMonthPeriod()
            else -> { }
        }
    }

    // ... (onCustomDateRangeSelected, setWeekPeriod, setMonthPeriod - без изменений)

    fun onCustomDateRangeSelected(startDateMillis: Long?, endDateMillis: Long?) {
        if (startDateMillis != null) {
            val start = java.time.Instant.ofEpochMilli(startDateMillis)
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            val end = if (endDateMillis != null) {
                java.time.Instant.ofEpochMilli(endDateMillis)
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            } else start
            currentDateStart = start
            currentDateEnd = end
            _uiState.update {
                it.copy(periodType = PeriodType.CUSTOM, dateRangeLabel = formatDateRange(start, end))
            }
            loadData()
        }
    }

    private fun setWeekPeriod() {
        val now = LocalDate.now()
        currentDateStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        currentDateEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        _uiState.update {
            it.copy(periodType = PeriodType.WEEK, dateRangeLabel = formatDateRange(currentDateStart, currentDateEnd))
        }
        loadData()
    }

    private fun setMonthPeriod() {
        val now = LocalDate.now()
        currentDateStart = now.with(TemporalAdjusters.firstDayOfMonth())
        currentDateEnd = now.with(TemporalAdjusters.lastDayOfMonth())
        val monthName = now.format(DateTimeFormatter.ofPattern("LLLL", Locale("ru")))
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        _uiState.update {
            it.copy(periodType = PeriodType.MONTH, selectedMonth = monthName, dateRangeLabel = formatDateRange(currentDateStart, currentDateEnd))
        }
        loadData()
    }

    fun onCategorySelected(categoryName: String) {
        val currentSelection = _uiState.value.selectedCategoryNames
        val newSelection = if (currentSelection.contains(categoryName)) {
            currentSelection - categoryName
        } else {
            currentSelection + categoryName
        }
        _uiState.update { it.copy(selectedCategoryNames = newSelection) }
        applyLocalFilters()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val startDateTime = currentDateStart.atStartOfDay()
            val endDateTime = currentDateEnd.atTime(LocalTime.MAX)

            getTransactionsUseCase.execute(query = currentQuery, startDate = startDateTime, endDate = endDateTime)
                .onSuccess { result ->
                    cachedResult = result
                    val chartData = result.categoryStats.map { stat ->
                        ChartDataUi(
                            categoryName = stat.categoryName,
                            amount = formatMoney(stat.amount),
                            color = Color(stat.color),
                            percentage = stat.percentage
                        )
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalExpense = formatMoney(result.totalExpense),
                            chartData = chartData,
                            // Не сбрасываем фильтры при перезагрузке, чтобы сохранить контекст поиска
                            // selectedCategoryNames = emptySet()
                        )
                    }
                    applyLocalFilters()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun applyLocalFilters() {
        val result = cachedResult ?: return
        val selectedCategories = _uiState.value.selectedCategoryNames
        val filteredGroups = result.groupedTransactions.mapNotNull { (date, transactions) ->
            val filteredTransactions = if (selectedCategories.isNotEmpty()) {
                transactions.filter { it.categoryName in selectedCategories }
            } else transactions

            if (filteredTransactions.isNotEmpty()) {
                TransactionGroupUi(
                    dateHeader = date,
                    dayTotal = "",
                    items = filteredTransactions.map { mapTransactionToUi(it) }
                )
            } else null
        }
        _uiState.update { it.copy(transactionGroups = filteredGroups) }
    }

    private fun mapTransactionToUi(tx: Transaction): TransactionUi {
        val isExpense = tx.type == TransactionType.EXPENSE
        val prefix = if (isExpense) "-" else "+"
        val color = if (isExpense) Color.Black else Color(0xFF43A047)
        return TransactionUi(tx.id, tx.merchantName ?: tx.description ?: "Операция", tx.categoryName, "$prefix${formatMoney(tx.amount)}", color, Color(tx.categoryColor))
    }

    private fun formatMoney(amount: Double) = "%,.0f ₽".format(amount).replace(',', ' ')
    private fun formatDateRange(start: LocalDate, end: LocalDate) = "${start.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru")))} - ${end.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru")))}"
}