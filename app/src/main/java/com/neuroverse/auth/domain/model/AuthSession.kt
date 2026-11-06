package com.neuroverse.auth.domain.model

/**
 * Immutable session model holding the user and session token.
 */
data class AuthSession(
    val user: User,
    val token: String
)
