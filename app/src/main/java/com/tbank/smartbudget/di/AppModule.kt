package com.tbank.smartbudget.di

import com.tbank.smartbudget.data.repository.BudgetRepositoryMockImpl
import com.tbank.smartbudget.domain.repository.BudgetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль Hilt для связывания интерфейсов с их реализациями.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Предоставляем Mock-реализацию репозитория на уровне приложения.
     */
    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryMockImpl: BudgetRepositoryMockImpl
    ): BudgetRepository

    // Use Cases предоставляются Hilt автоматически, так как они имеют @Inject constructor.
}
