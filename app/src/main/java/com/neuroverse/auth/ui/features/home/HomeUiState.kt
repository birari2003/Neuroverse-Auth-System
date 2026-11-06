package com.neuroverse.auth.ui.features.home

import com.neuroverse.auth.domain.model.User

data class HomeUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val showLogoutConfirmDialog: Boolean = false
)

sealed interface HomeUiEvent {
    data object RequestLogout : HomeUiEvent
    data object DismissLogoutDialog : HomeUiEvent
    data object ConfirmLogout : HomeUiEvent
}

sealed interface HomeUiEffect {
    data object NavigateToWelcome : HomeUiEffect
}
