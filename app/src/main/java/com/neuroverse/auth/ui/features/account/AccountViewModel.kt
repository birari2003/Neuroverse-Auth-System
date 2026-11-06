package com.neuroverse.auth.ui.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuroverse.auth.core.common.DispatcherProvider
import com.neuroverse.auth.domain.usecase.GetCurrentUserUseCase
import com.neuroverse.auth.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Shared ViewModel for the logged-in screens (Home and Profile): loads the current user
 * and handles the logout confirmation flow.
 *
 * Navigation after logout is not done here: logout clears the session, and the auth guard
 * in AppNavHost reacts to that by sending the user to Welcome.
 */
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun onEvent(event: AccountUiEvent) {
        when (event) {
            AccountUiEvent.RequestLogout -> _uiState.update { it.copy(showLogoutConfirmDialog = true) }
            AccountUiEvent.DismissLogoutDialog -> _uiState.update { it.copy(showLogoutConfirmDialog = false) }
            AccountUiEvent.ConfirmLogout -> logout()
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch(dispatchers.main) {
            val user = getCurrentUserUseCase()
            _uiState.update { it.copy(user = user) }
        }
    }

    private fun logout() {
        viewModelScope.launch(dispatchers.main) {
            _uiState.update { it.copy(showLogoutConfirmDialog = false, isLoading = true) }
            logoutUseCase()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
