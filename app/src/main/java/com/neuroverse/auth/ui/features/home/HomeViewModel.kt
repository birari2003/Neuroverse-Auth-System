package com.neuroverse.auth.ui.features.home

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
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<HomeUiEffect>(Channel.BUFFERED)
    val effects: Flow<HomeUiEffect> = _effects.receiveAsFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch(dispatchers.main) {
            val user = getCurrentUserUseCase()
            _uiState.update { it.copy(user = user) }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.RequestLogout -> _uiState.update { it.copy(showLogoutConfirmDialog = true) }
            HomeUiEvent.DismissLogoutDialog -> _uiState.update { it.copy(showLogoutConfirmDialog = false) }
            HomeUiEvent.ConfirmLogout -> executeLogout()
        }
    }

    private fun executeLogout() {
        viewModelScope.launch(dispatchers.main) {
            _uiState.update { it.copy(showLogoutConfirmDialog = false, isLoading = true) }
            logoutUseCase()
            _uiState.update { it.copy(isLoading = false) }
            _effects.send(HomeUiEffect.NavigateToWelcome)
        }
    }
}
