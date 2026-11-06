package com.neuroverse.auth.domain.usecase

import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AppResult<Unit> {
        return repository.logout()
    }
}
