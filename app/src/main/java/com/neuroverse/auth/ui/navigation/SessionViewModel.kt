package com.neuroverse.auth.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuroverse.auth.domain.usecase.ObserveAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * App-wide session status used by navigation (Splash routing and the auth guard).
 * [Loading] means the repository has not reported yet, so it is never confused with "logged out".
 */
sealed interface SessionState {
    data object Loading : SessionState
    data object Authenticated : SessionState
    data object Unauthenticated : SessionState
}

/**
 * Activity-scoped ViewModel that turns the repository auth stream into a [SessionState].
 * Single source of truth for "is the user logged in?": used by the system splash screen
 * (stay visible while [SessionState.Loading]), the start destination, and the auth guard.
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {

    val sessionState: StateFlow<SessionState> = observeAuthStateUseCase()
        .map { session ->
            if (session != null) SessionState.Authenticated else SessionState.Unauthenticated
        }
        .stateIn(
            scope = viewModelScope,
            // Eagerly: MainActivity reads .value for the splash before any UI collects it.
            started = SharingStarted.Eagerly,
            initialValue = SessionState.Loading
        )
}
