package com.neuroverse.auth.ui.features.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.core.common.DispatcherProvider
import com.neuroverse.auth.core.common.asUiText
import com.neuroverse.auth.domain.usecase.SendPasswordResetUseCase
import com.neuroverse.auth.domain.validation.ValidateEmail
import com.neuroverse.auth.domain.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sendPasswordResetUseCase: SendPasswordResetUseCase,
    private val validateEmail: ValidateEmail,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.value, emailError = null) }
            }
            ForgotPasswordUiEvent.Submit -> submit()
            ForgotPasswordUiEvent.ResetForm -> {
                _uiState.update { ForgotPasswordUiState() }
            }
        }
    }

    private fun submit() {
        if (_uiState.value.isLoading) return

        val email = _uiState.value.email
        val emailRes = validateEmail(email)
        if (!emailRes.isValid) {
            _uiState.update {
                it.copy(emailError = (emailRes as? ValidationResult.Invalid)?.error)
            }
            return
        }

        viewModelScope.launch(dispatchers.main) {
            _uiState.update { it.copy(isLoading = true, formError = null) }
            val result = sendPasswordResetUseCase(email)
            _uiState.update { it.copy(isLoading = false) }

            when (result) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSuccess = true,
                            submittedEmail = email
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(formError = result.error.asUiText()) }
                }
            }
        }
    }
}
