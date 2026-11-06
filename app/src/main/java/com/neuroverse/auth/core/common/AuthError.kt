package com.neuroverse.auth.core.common

import com.neuroverse.auth.R

/**
 * Sealed domain representations of authentication errors.
 */
sealed interface AuthError {
    data object InvalidCredentials : AuthError
    data object EmailAlreadyInUse : AuthError
    data object Network : AuthError
    data object Unknown : AuthError
}

/**
 * Maps domain AuthError to user-friendly localized UiText in one centralized location.
 */
fun AuthError.asUiText(): UiText = when (this) {
    AuthError.InvalidCredentials -> UiText.StringResource(R.string.error_invalid_credentials)
    AuthError.EmailAlreadyInUse -> UiText.StringResource(R.string.error_email_already_registered)
    AuthError.Network -> UiText.StringResource(R.string.error_network_connection)
    AuthError.Unknown -> UiText.StringResource(R.string.error_unknown_occurred)
}
