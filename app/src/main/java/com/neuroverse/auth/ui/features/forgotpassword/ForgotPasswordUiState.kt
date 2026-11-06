package com.neuroverse.auth.ui.features.forgotpassword

import com.neuroverse.auth.core.common.UiText

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: UiText? = null,
    val formError: UiText? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val submittedEmail: String = ""
)

sealed interface ForgotPasswordUiEvent {
    data class EmailChanged(val value: String) : ForgotPasswordUiEvent
    data object Submit : ForgotPasswordUiEvent
    data object ResetForm : ForgotPasswordUiEvent
}
