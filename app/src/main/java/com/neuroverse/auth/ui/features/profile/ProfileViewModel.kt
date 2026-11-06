package com.neuroverse.auth.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuroverse.auth.core.common.DispatcherProvider
import com.neuroverse.auth.domain.usecase.GetCurrentUserUseCase
import com.neuroverse.auth.domain.usecase.LogoutUseCase
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
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<ProfileUiEffect>(Channel.BUFFERED)
    val effects: Flow<ProfileUiEffect> = _effects.receiveAsFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch(dispatchers.main) {
            val user = getCurrentUserUseCase()
            _uiState.update { it.copy(user = user) }
        }
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.RequestLogout -> _uiState.update { it.copy(showLogoutConfirmDialog = true) }
            ProfileUiEvent.DismissLogoutDialog -> _uiState.update { it.copy(showLogoutConfirmDialog = false) }
            ProfileUiEvent.ConfirmLogout -> executeLogout()
        }
    }

    private fun executeLogout() {
        viewModelScope.launch(dispatchers.main) {
            _uiState.update { it.copy(showLogoutConfirmDialog = false, isLoading = true) }
            logoutUseCase()
            _uiState.update { it.copy(isLoading = false) }
            _effects.send(ProfileUiEffect.NavigateToWelcome)
        }
    }
}
