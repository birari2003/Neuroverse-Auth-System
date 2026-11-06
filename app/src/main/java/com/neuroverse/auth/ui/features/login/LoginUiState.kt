package com.neuroverse.auth.ui.features.login

import com.neuroverse.auth.core.common.UiText

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val formError: UiText? = null,
    val isLoading: Boolean = false,
    val hasSubmittedOnce: Boolean = false
)

sealed interface LoginUiEvent {
    data class EmailChanged(val value: String) : LoginUiEvent
    data class PasswordChanged(val value: String) : LoginUiEvent
    data object Submit : LoginUiEvent
}

sealed interface LoginUiEffect {
    data object NavigateToHome : LoginUiEffect
    data object NavigateToForgotPassword : LoginUiEffect
    data object NavigateToRegister : LoginUiEffect
}
