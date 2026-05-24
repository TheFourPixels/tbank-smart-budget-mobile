package com.tbank.smartbudget.data.repository

import com.tbank.smartbudget.core.network.remote.api.GoalApi
import com.tbank.smartbudget.core.network.remote.dto.CreateGoalRequest
import com.tbank.smartbudget.core.network.remote.dto.GoalContributionRequest
import com.tbank.smartbudget.data.domain.model.Goal
import com.tbank.smartbudget.data.domain.repository.GoalRepository
import com.tbank.smartbudget.data.repository.mappers.toDomain
import java.time.LocalDate
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val api: GoalApi
) : GoalRepository {

    override suspend fun getGoals(): Result<List<Goal>> {
        return try {
            val now = LocalDate.now()
            val activeGoals = api.listActive(now.year, now.monthValue)
            Result.success(activeGoals.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGoalDetails(id: Long): Result<Goal> {
        return try {
            val dto = api.get(id)
            Result.success(dto.toDomain())
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
            val dto = api.create(request)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteGoal(id: Long): Result<Unit> {
        return try {
            api.delete(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun contributeToGoal(id: Long, amount: Double): Result<Goal> {
        return try {
            val dto = api.contribute(id, GoalContributionRequest(amount))
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
