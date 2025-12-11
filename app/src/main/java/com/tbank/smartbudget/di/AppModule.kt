package com.tbank.smartbudget.di

import com.tbank.smartbudget.data.repository.BudgetRepositoryMockImpl
import com.tbank.smartbudget.data.repository.CategorySearchRepositoryImpl
import com.tbank.smartbudget.domain.repository.BudgetRepository
import com.tbank.smartbudget.domain.repository.CategorySearchRepository
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
        MockCategorySearchRepositoryImpl: CategorySearchRepositoryImpl
    ): CategorySearchRepository
}