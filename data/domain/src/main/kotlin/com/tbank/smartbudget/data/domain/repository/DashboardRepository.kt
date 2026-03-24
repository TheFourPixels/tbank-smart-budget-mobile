package com.tbank.smartbudget.data.domain.repository

import com.tbank.smartbudget.data.domain.model.DashboardData

interface DashboardRepository {
    suspend fun getDashboardSummary(month: Int?, year: Int?): Result<DashboardData>
}