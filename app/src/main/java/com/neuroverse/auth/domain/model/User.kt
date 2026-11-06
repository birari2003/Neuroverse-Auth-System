package com.neuroverse.auth.domain.model

/**
 * Immutable domain representation of a user.
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val createdAtMillis: Long
)
