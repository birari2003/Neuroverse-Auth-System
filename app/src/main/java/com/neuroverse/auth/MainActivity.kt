package com.neuroverse.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.neuroverse.auth.ui.navigation.AppNavHost
import com.neuroverse.auth.ui.theme.AuthAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single activity architecture hosting the Compose Navigation graph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install Android 12+ SplashScreen to prevent white flash
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AuthAppTheme {
                AppNavHost()
            }
        }
    }
}
