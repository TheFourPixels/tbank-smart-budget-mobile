package com.tbank.smartbudget.di

import com.tbank.smartbudget.data.repository.BudgetRepositoryMockImpl
import com.tbank.smartbudget.domain.repository.BudgetRepository
import com.tbank.smartbudget.domain.repository.CategorySearchRepository // Импорт нового интерфейса
import com.tbank.smartbudget.data.repository.MockCategorySearchRepositoryImpl
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
     * Предоставляем Mock-реализацию репозитория BudgetRepository.
     */
    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryMockImpl: BudgetRepositoryMockImpl
    ): BudgetRepository

    /**
     * Предоставляем Mock-реализацию репозитория CategorySearchRepository.
     */
    @Binds
    @Singleton
    abstract fun bindCategorySearchRepository(
        mockCategorySearchRepositoryImpl: MockCategorySearchRepositoryImpl
    ): CategorySearchRepository

    // Use Cases предоставляются Hilt автоматически, так как они имеют @Inject constructor.
}