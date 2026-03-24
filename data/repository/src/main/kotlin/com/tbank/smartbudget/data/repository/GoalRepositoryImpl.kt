package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.network.remote.api.GoalApi
import com.tbank.smartbudget.core.network.remote.dto.CreateGoalRequest
import com.tbank.smartbudget.data.domain.model.Goal
import com.tbank.smartbudget.data.domain.repository.GoalRepository
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val api: GoalApi
) : GoalRepository {

    override suspend fun getGoals(): Result<List<Goal>> {
        return try {
            val dtos = api.getGoals()
            val goals = dtos.map { dto ->
                // Рассчитываем процент, если его нет в DTO напрямую (но в Dashboard есть)
                val progress = if (dto.targetAmount > 0) {
                    ((dto.currentAmount / dto.targetAmount) * 100).toInt()
                } else 0

                Goal(
                    id = dto.id,
                    name = dto.name,
                    targetAmount = dto.targetAmount,
                    savedAmount = dto.currentAmount,
                    deadline = dto.deadline,
                    progressPercent = progress
                )
            }
            Result.success(goals)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createGoal(
        name: String,
        targetAmount: Double,
        deadline: String?
    ): Result<Goal> {
        return try {
            val request = CreateGoalRequest(name, targetAmount, deadline)
            val dto = api.createGoal(request)
            Goal(
                id = dto.id,
                name = dto.name,
                targetAmount = dto.targetAmount,
                savedAmount = dto.currentAmount,
                deadline = dto.deadline,
                progressPercent = 0
            ).let { Result.success(it) }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteGoal(id: Long): Result<Unit> {
        return try {
            api.deleteGoal(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}