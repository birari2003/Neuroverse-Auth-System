package com.neuroverse.auth.domain.usecase

import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.domain.model.User
import com.neuroverse.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): AppResult<User> {
        return repository.register(name.trim(), email.trim().lowercase(), password)
    }
}
