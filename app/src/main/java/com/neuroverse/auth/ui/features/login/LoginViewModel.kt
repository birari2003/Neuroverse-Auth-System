package com.neuroverse.auth.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.core.common.DispatcherProvider
import com.neuroverse.auth.core.common.asUiText
import com.neuroverse.auth.domain.usecase.LoginUseCase
import com.neuroverse.auth.domain.validation.ValidateEmail
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<LoginUiEffect>(Channel.BUFFERED)
    val effects: Flow<LoginUiEffect> = _effects.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.value) }
                if (_uiState.value.hasSubmittedOnce) validateFields()
            }
            is LoginUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.value) }
                if (_uiState.value.hasSubmittedOnce) validateFields()
            }
            LoginUiEvent.Submit -> submit()
        }
    }

    private fun validateFields(): Boolean {
        val state = _uiState.value
        val emailRes = validateEmail(state.email)
        val passRes = validatePassword.validateForLogin(state.password)

        val hasError = !emailRes.isValid || !passRes.isValid

        _uiState.update {
            it.copy(
                emailError = (emailRes as? ValidationResult.Invalid)?.error,
                passwordError = (passRes as? ValidationResult.Invalid)?.error,
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
            val result = loginUseCase(state.email, state.password)
            _uiState.update { it.copy(isLoading = false) }

            when (result) {
                is AppResult.Success -> {
                    _effects.send(LoginUiEffect.NavigateToHome)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(formError = result.error.asUiText()) }
                }
            }
        }
    }
}
