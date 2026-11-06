package com.neuroverse.auth.domain.usecase

import com.neuroverse.auth.domain.model.AuthSession
import com.neuroverse.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<AuthSession?> = repository.authState
}
