package com.tbank.smartbudget.data.domain.usecase

import com.tbank.smartbudget.core.network.remote.AppResult
import com.tbank.smartbudget.data.domain.model.User
import com.tbank.smartbudget.data.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(name: String): AppResult<User> = repository.updateProfile(name)
}
