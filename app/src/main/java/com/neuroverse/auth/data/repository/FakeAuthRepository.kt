package com.neuroverse.auth.data.repository

import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.core.common.AuthError
import com.neuroverse.auth.domain.model.AuthSession
import com.neuroverse.auth.domain.model.User
import com.neuroverse.auth.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory test and demo implementation of AuthRepository.
 * Simulates network latency, handles state flow emission, and avoids logging passwords.
 */
@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {

    private data class StoredAccount(
        val user: User,
        val passwordHash: Int
    )

    private val mutex = Mutex()
    private val accounts = mutableMapOf<String, StoredAccount>()
    private val _authState = MutableStateFlow<AuthSession?>(null)
    override val authState: Flow<AuthSession?> = _authState.asStateFlow()

    // Configurable simulated failure flag for testing and demoing ErrorBanner
    var simulateNetworkError: Boolean = false

    init {
        // Seed default demo user: demo@neuroverse.com / Pass1234
        val demoUser = User(
            id = "usr_demo_001",
            name = "Alex Mercer",
            email = "demo@neuroverse.com",
            createdAtMillis = System.currentTimeMillis() - 86_400_000L * 30
        )
        accounts[demoUser.email] = StoredAccount(demoUser, "Pass1234".hashCode())
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): AppResult<User> {
        delay(1000)
        if (simulateNetworkError) return AppResult.Error(AuthError.Network)

        mutex.withLock {
            if (accounts.containsKey(email)) {
                return AppResult.Error(AuthError.EmailAlreadyInUse)
            }
            val newUser = User(
                id = "usr_" + UUID.randomUUID().toString().take(8),
                name = name,
                email = email,
                createdAtMillis = System.currentTimeMillis()
            )
            accounts[email] = StoredAccount(newUser, password.hashCode())
            val session = AuthSession(newUser, "token_" + UUID.randomUUID().toString())
            _authState.value = session
            return AppResult.Success(newUser)
        }
    }

    override suspend fun login(email: String, password: String): AppResult<User> {
        delay(1000)
        if (simulateNetworkError) return AppResult.Error(AuthError.Network)

        mutex.withLock {
            val account = accounts[email]
            if (account == null || account.passwordHash != password.hashCode()) {
                // Return generic invalid credentials (never reveal if email exists)
                return AppResult.Error(AuthError.InvalidCredentials)
            }
            val session = AuthSession(account.user, "token_" + UUID.randomUUID().toString())
            _authState.value = session
            return AppResult.Success(account.user)
        }
    }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit> {
        delay(1000)
        if (simulateNetworkError) return AppResult.Error(AuthError.Network)

        // Always succeed to prevent user enumeration attacks
        return AppResult.Success(Unit)
    }

    override suspend fun logout(): AppResult<Unit> {
        delay(300)
        _authState.value = null
        return AppResult.Success(Unit)
    }

    override suspend fun getCurrentUser(): User? = _authState.value?.user
}
