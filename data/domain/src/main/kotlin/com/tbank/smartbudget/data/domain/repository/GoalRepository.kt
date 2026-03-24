package com.tbank.smartbudget.data.domain.repository

import com.tbank.smartbudget.data.domain.model.Goal

interface GoalRepository {
    suspend fun getGoals(): Result<List<Goal>>
    suspend fun createGoal(name: String, targetAmount: Double, deadline: String?): Result<Goal>
    suspend fun deleteGoal(id: Long): Result<Unit>
}