package com.neuroverse.auth.ui.features.account

import com.neuroverse.auth.domain.model.User

/**
 * Shared UI state for the logged-in screens (Home and Profile).
 */
data class AccountUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val showLogoutConfirmDialog: Boolean = false
)

sealed interface AccountUiEvent {
    data object RequestLogout : AccountUiEvent
    data object DismissLogoutDialog : AccountUiEvent
    data object ConfirmLogout : AccountUiEvent
}
