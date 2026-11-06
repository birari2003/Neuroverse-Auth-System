package com.neuroverse.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.UserProfileChangeRequest
import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.core.common.DispatcherProvider
import com.neuroverse.auth.data.mapper.toAuthError
import com.neuroverse.auth.data.mapper.toUser
import com.neuroverse.auth.domain.model.AuthSession
import com.neuroverse.auth.domain.model.User
import com.neuroverse.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Production [AuthRepository] backed by Firebase Authentication (Email/Password).
 *
 * Firebase hashes passwords server-side, enforces unique emails, sends reset emails,
 * and persists + refreshes the session tokens on the device. This class only adapts
 * Firebase's API to our domain types and errors.
 */
@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dispatchers: DispatcherProvider
) : AuthRepository {

    /**
     * Emits the current session immediately, then on every sign-in / sign-out
     * (including Firebase signing the user out when a token can't be refreshed).
     */
    override val authState: Flow<AuthSession?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.let { AuthSession(it.toUser()) })
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun register(name: String, email: String, password: String): AppResult<User> =
        safeCall {
            val firebaseUser = requireNotNull(
                firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            )
            val profile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            firebaseUser.updateProfile(profile).await()
            firebaseUser.toUser()
        }

    override suspend fun login(email: String, password: String): AppResult<User> =
        safeCall {
            requireNotNull(
                firebaseAuth.signInWithEmailAndPassword(email, password).await().user
            ).toUser()
        }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit> =
        safeCall {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
            } catch (e: FirebaseAuthInvalidUserException) {
                // No account for this email: report success anyway so the screen
                // can't be used to discover which emails are registered.
            }
        }

    override suspend fun logout(): AppResult<Unit> =
        safeCall { firebaseAuth.signOut() }

    override suspend fun getCurrentUser(): User? = firebaseAuth.currentUser?.toUser()

    /**
     * Runs a Firebase call on the IO dispatcher and converts any failure into an
     * [AppResult.Error] through the single [toAuthError] mapper.
     * Coroutine cancellation is re-thrown so it is never reported as an auth error.
     */
    private suspend fun <T> safeCall(block: suspend () -> T): AppResult<T> =
        withContext(dispatchers.io) {
            try {
                AppResult.Success(block())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AppResult.Error(e.toAuthError())
            }
        }
}
