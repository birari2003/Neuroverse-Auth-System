package com.neuroverse.auth.domain.usecase

import com.neuroverse.auth.domain.model.User
import com.neuroverse.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): User? = repository.getCurrentUser()
}
