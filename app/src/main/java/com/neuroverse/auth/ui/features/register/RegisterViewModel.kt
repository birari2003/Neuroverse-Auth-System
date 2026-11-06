package com.neuroverse.auth.ui.features.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuroverse.auth.R
import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.core.common.AuthError
import com.neuroverse.auth.core.common.DispatcherProvider
import com.neuroverse.auth.core.common.UiText
import com.neuroverse.auth.core.common.asUiText
import com.neuroverse.auth.domain.usecase.RegisterUseCase
import com.neuroverse.auth.domain.validation.ValidateConfirmPassword
import com.neuroverse.auth.domain.validation.ValidateEmail
import com.neuroverse.auth.domain.validation.ValidateName
import com.neuroverse.auth.domain.validation.ValidatePassword
import com.neuroverse.auth.domain.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val validateName: ValidateName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<RegisterUiEffect>(Channel.BUFFERED)
    val effects: Flow<RegisterUiEffect> = _effects.receiveAsFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.value) }
                if (_uiState.value.hasSubmittedOnce) validateFields()
            }
            is RegisterUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.value) }
                if (_uiState.value.hasSubmittedOnce) validateFields()
            }
            is RegisterUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.value) }
                if (_uiState.value.hasSubmittedOnce) validateFields()
            }
            is RegisterUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.value) }
                if (_uiState.value.hasSubmittedOnce) validateFields()
            }
            RegisterUiEvent.Submit -> submit()
        }
    }

    private fun validateFields(): Boolean {
        val state = _uiState.value
        val nameRes = validateName(state.name)
        val emailRes = validateEmail(state.email)
        val passwordRes = validatePassword.validateForRegistration(state.password)
        val confirmRes = validateConfirmPassword(state.password, state.confirmPassword)

        val hasError = listOf(nameRes, emailRes, passwordRes, confirmRes).any { !it.isValid }

        _uiState.update {
            it.copy(
                nameError = (nameRes as? ValidationResult.Invalid)?.error,
                emailError = (emailRes as? ValidationResult.Invalid)?.error,
                passwordError = (passwordRes as? ValidationResult.Invalid)?.error,
                confirmPasswordError = (confirmRes as? ValidationResult.Invalid)?.error,
                formError = null
            )
        }
        return !hasError
    }

    private fun submit() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(hasSubmittedOnce = true) }

        if (!validateFields()) return

        val state = _uiState.value
        viewModelScope.launch(dispatchers.main) {
            _uiState.update { it.copy(isLoading = true, formError = null) }
            val result = registerUseCase(state.name, state.email, state.password)
            _uiState.update { it.copy(isLoading = false) }

            when (result) {
                is AppResult.Success -> {
                    _effects.send(RegisterUiEffect.ShowSuccessMessage(UiText.StringResource(R.string.registration_successful)))
                    _effects.send(RegisterUiEffect.NavigateToHome)
                }
                is AppResult.Error -> {
                    if (result.error == AuthError.EmailAlreadyInUse) {
                        _uiState.update { it.copy(emailError = result.error.asUiText()) }
                    } else {
                        _uiState.update { it.copy(formError = result.error.asUiText()) }
                    }
                }
            }
        }
    }
}
