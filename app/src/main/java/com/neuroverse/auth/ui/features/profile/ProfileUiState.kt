package com.neuroverse.auth.ui.features.profile

import com.neuroverse.auth.domain.model.User

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val showLogoutConfirmDialog: Boolean = false
)

sealed interface ProfileUiEvent {
    data object RequestLogout : ProfileUiEvent
    data object DismissLogoutDialog : ProfileUiEvent
    data object ConfirmLogout : ProfileUiEvent
}

sealed interface ProfileUiEffect {
    data object NavigateToWelcome : ProfileUiEffect
}
