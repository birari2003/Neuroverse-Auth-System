package com.neuroverse.auth.ui.features.register

import com.neuroverse.auth.core.common.UiText

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: UiText? = null,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val confirmPasswordError: UiText? = null,
    val formError: UiText? = null,
    val isLoading: Boolean = false,
    val hasSubmittedOnce: Boolean = false
)

sealed interface RegisterUiEvent {
    data class NameChanged(val value: String) : RegisterUiEvent
    data class EmailChanged(val value: String) : RegisterUiEvent
    data class PasswordChanged(val value: String) : RegisterUiEvent
    data class ConfirmPasswordChanged(val value: String) : RegisterUiEvent
    data object Submit : RegisterUiEvent
}

sealed interface RegisterUiEffect {
    data object NavigateToHome : RegisterUiEffect
    data class ShowSuccessMessage(val message: UiText) : RegisterUiEffect
}
