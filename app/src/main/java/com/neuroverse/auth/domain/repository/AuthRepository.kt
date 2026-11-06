package com.neuroverse.auth.domain.repository

import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.domain.model.AuthSession
import com.neuroverse.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Domain authentication repository interface.
 * Implemented by FakeAuthRepository now and easily swapped with FirebaseAuthRepository later.
 */
interface AuthRepository {
    val authState: Flow<AuthSession?>

    suspend fun register(name: String, email: String, password: String): AppResult<User>
    suspend fun login(email: String, password: String): AppResult<User>
    suspend fun sendPasswordReset(email: String): AppResult<Unit>
    suspend fun logout(): AppResult<Unit>
    suspend fun getCurrentUser(): User?
}
