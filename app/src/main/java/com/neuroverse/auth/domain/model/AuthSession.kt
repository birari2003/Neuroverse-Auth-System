package com.neuroverse.auth.domain.model

/**
 * Immutable model of an active login session.
 *
 * Tokens are deliberately not part of this model: the auth provider (Firebase) stores,
 * encrypts and refreshes them itself, so they never enter app state or the UI.
 */
data class AuthSession(
    val user: User
)
