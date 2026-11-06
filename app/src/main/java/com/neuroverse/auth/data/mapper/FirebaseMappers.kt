package com.neuroverse.auth.data.mapper

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.neuroverse.auth.core.common.AuthError
import com.neuroverse.auth.domain.model.User

/**
 * The single place where Firebase exceptions become domain [AuthError]s.
 *
 * Wrong password, unknown email and disabled account all map to the same
 * [AuthError.InvalidCredentials], so the UI never reveals which part was wrong.
 * Exception messages are never logged or shown (they can contain the email).
 */
internal fun Throwable.toAuthError(): AuthError = when (this) {
    is FirebaseAuthUserCollisionException -> AuthError.EmailAlreadyInUse
    is FirebaseAuthInvalidCredentialsException,
    is FirebaseAuthInvalidUserException -> AuthError.InvalidCredentials
    is FirebaseNetworkException -> AuthError.Network
    is FirebaseTooManyRequestsException -> AuthError.TooManyRequests
    else -> AuthError.Unknown
}

/**
 * Maps a Firebase user to our domain [User].
 * "Member since" comes from the account creation timestamp Firebase keeps for us.
 */
internal fun FirebaseUser.toUser(): User = User(
    id = uid,
    name = displayName.orEmpty(),
    email = email.orEmpty(),
    createdAtMillis = metadata?.creationTimestamp ?: 0L
)
