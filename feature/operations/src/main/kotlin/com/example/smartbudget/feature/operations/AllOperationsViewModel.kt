package com.example.smartbudget.feature.operations

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.core.ui.common.CategoryColorMapper
import com.tbank.smartbudget.data.domain.model.Transaction
import com.tbank.smartbudget.data.domain.model.TransactionType
import com.tbank.smartbudget.data.domain.usecase.GetTransactionsUseCase
import com.tbank.smartbudget.data.domain.usecase.TransactionsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AllOperationsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : BaseViewModel<AllOperationsUiState, AllOperationsIntent, AllOperationsEffect>(
    AllOperationsUiState(isLoading = true)
) {

    private var cachedResult: TransactionsResult? = null
    private var currentQuery = ""
    private var currentDateStart: LocalDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
    private var currentDateEnd: LocalDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())

    init {
        onIntent(AllOperationsIntent.OnPeriodChanged(PeriodType.MONTH))
    }

    override fun onIntent(intent: AllOperationsIntent) {
        when (intent) {
            AllOperationsIntent.LoadData -> loadData()
            is AllOperationsIntent.OnSearchQueryChanged -> {
                currentQuery = intent.query
                loadData()
            }
            is AllOperationsIntent.OnCategorySearchResult -> handleCategorySearchResult(intent.categoryName)
            is AllOperationsIntent.OnPeriodChanged -> handlePeriodChange(intent.periodType)
            is AllOperationsIntent.OnCustomDateRangeSelected -> handleCustomDateRange(intent.startMillis, intent.endMillis)
            is AllOperationsIntent.OnCategorySelected -> handleCategorySelection(intent.categoryName)
            AllOperationsIntent.OnBackClick -> sendEffect(AllOperationsEffect.NavigateBack)
            AllOperationsIntent.OnSearchClick -> sendEffect(AllOperationsEffect.NavigateToSearch)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
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
                    updateState {
                        copy(
                            isLoading = false,
                            totalExpense = formatMoney(result.totalExpense),
                            chartData = chartData,
                            dateRangeLabel = formatDateRange(currentDateStart, currentDateEnd)
                        )
                    }
                    applyLocalFilters()
                }
                .onFailure { error ->
                    updateState { copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun applyLocalFilters() {
        val result = cachedResult ?: return
        val selectedCategories = currentState.selectedCategoryNames

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
        updateState { copy(transactionGroups = filteredGroups) }
    }

    private fun mapTransactionToUi(tx: Transaction): TransactionUi {
        val isExpense = tx.type == TransactionType.EXPENSE
        return TransactionUi(
            id = tx.id,
            title = tx.merchantName ?: tx.description ?: "Операция",
            subtitle = tx.categoryName,
            amount = "${if (isExpense) "-" else "+"}${formatMoney(tx.amount)}",
            amountColor = if (isExpense) Color.Black else Color(0xFF43A047),
            iconColor = Color(CategoryColorMapper.getColorForId(tx.categoryId.value))
        )
    }

    private fun handlePeriodChange(type: PeriodType) {
        val now = LocalDate.now()
        when (type) {
            PeriodType.WEEK -> {
                currentDateStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                currentDateEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            }
            PeriodType.MONTH -> {
                currentDateStart = now.with(TemporalAdjusters.firstDayOfMonth())
                currentDateEnd = now.with(TemporalAdjusters.lastDayOfMonth())
            }
            else -> return
        }
        updateState { copy(periodType = type) }
        loadData()
    }

    private fun handleCustomDateRange(startMillis: Long?, endMillis: Long?) {
        startMillis?.let {
            val start = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            val end = endMillis?.let { e -> Instant.ofEpochMilli(e).atZone(ZoneId.systemDefault()).toLocalDate() } ?: start
            currentDateStart = start
            currentDateEnd = end
            updateState { copy(periodType = PeriodType.CUSTOM) }
            loadData()
        }
    }

    private fun handleCategorySelection(name: String) {
        val current = currentState.selectedCategoryNames
        val next = if (current.contains(name)) current - name else current + name
        updateState { copy(selectedCategoryNames = next) }
        applyLocalFilters()
    }

    private fun handleCategorySearchResult(name: String) {
        if (!currentState.selectedCategoryNames.contains(name)) {
            updateState { copy(selectedCategoryNames = selectedCategoryNames + name) }
            applyLocalFilters()
        }
    }

    private fun formatMoney(amount: Double) = "%,.0f ₽".format(amount).replace(',', ' ')
    private fun formatDateRange(start: LocalDate, end: LocalDate) =
        "${start.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru")))} - ${end.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru")))}"
}