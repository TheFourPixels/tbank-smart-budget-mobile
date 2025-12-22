package com.tbank.smartbudget.di

import com.tbank.smartbudget.data.repository.BudgetRepositoryMockImpl
import com.tbank.smartbudget.data.repository.MockCategorySearchRepositoryImpl
import com.tbank.smartbudget.data.repository.MockTransactionRepositoryImpl
import com.tbank.smartbudget.domain.repository.BudgetRepository
import com.tbank.smartbudget.domain.repository.CategorySearchRepository
import com.tbank.smartbudget.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryMockImpl: BudgetRepositoryMockImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindCategorySearchRepository(
        MockCategorySearchRepositoryImpl: MockCategorySearchRepositoryImpl
    ): CategorySearchRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: MockTransactionRepositoryImpl
    ): TransactionRepository
}