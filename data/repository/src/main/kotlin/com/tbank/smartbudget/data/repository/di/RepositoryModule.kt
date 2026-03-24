package com.tbank.smartbudget.data.repository.di

import com.tbank.smartbudget.data.repository.AuthRepositoryImpl
import com.tbank.smartbudget.data.repository.BudgetRepositoryImpl
import com.tbank.smartbudget.data.repository.CategorySearchRepositoryImpl
import com.tbank.smartbudget.data.repository.DashboardRepositoryImpl
import com.tbank.smartbudget.data.repository.GoalRepositoryImpl
import com.tbank.smartbudget.data.repository.TransactionRepositoryImpl
import com.tbank.smartbudget.data.domain.repository.AuthRepository
import com.tbank.smartbudget.data.domain.repository.BudgetRepository
import com.tbank.smartbudget.data.domain.repository.CategorySearchRepository
import com.tbank.smartbudget.data.domain.repository.DashboardRepository
import com.tbank.smartbudget.data.domain.repository.GoalRepository
import com.tbank.smartbudget.data.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategorySearchRepository(
        categorySearchRepositoryImpl: CategorySearchRepositoryImpl
    ): CategorySearchRepository

    @Binds
    @Singleton
    abstract fun bindGoalRepository(
        goalRepositoryImpl: GoalRepositoryImpl
    ): GoalRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository
}