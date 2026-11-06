package com.neuroverse.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.neuroverse.auth.ui.navigation.AppNavHost
import com.neuroverse.auth.ui.navigation.SessionState
import com.neuroverse.auth.ui.navigation.SessionViewModel
import com.neuroverse.auth.ui.theme.AuthAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single activity architecture hosting the Compose Navigation graph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Same activity-scoped instance that AppNavHost gets via hiltViewModel().
    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // System splash screen (backported to older Android by core-splashscreen).
        // It stays visible until we know whether the user is logged in.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            sessionViewModel.sessionState.value == SessionState.Loading
        }

        enableEdgeToEdge()

        setContent {
            AuthAppTheme {
                AppNavHost()
            }
        }
    }
}
