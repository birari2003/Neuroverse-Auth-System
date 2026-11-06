package com.neuroverse.auth.domain.usecase

import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.domain.model.User
import com.neuroverse.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AppResult<User> {
        return repository.login(email.trim().lowercase(), password)
    }
}
