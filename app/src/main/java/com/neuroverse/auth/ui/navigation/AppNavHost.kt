package com.neuroverse.auth.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neuroverse.auth.ui.features.forgotpassword.ForgotPasswordScreen
import com.neuroverse.auth.ui.features.home.HomeScreen
import com.neuroverse.auth.ui.features.login.LoginScreen
import com.neuroverse.auth.ui.features.profile.ProfileScreen
import com.neuroverse.auth.ui.features.register.RegisterScreen
import com.neuroverse.auth.ui.features.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Activity-scoped: one session observer for the whole app.
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val sessionState by sessionViewModel.sessionState.collectAsStateWithLifecycle()

    // Auth guard: if the session ends (logout, expiry, revocation) while a protected
    // screen is visible, send the user to Welcome and wipe the back stack.
    LaunchedEffect(sessionState) {
        if (sessionState != SessionState.Unauthenticated) return@LaunchedEffect
        val current = navController.currentBackStackEntry?.destination ?: return@LaunchedEffect
        if (PROTECTED_ROUTES.any { current.hasRoute(it) }) {
            navController.navigateToWelcome()
        }
    }

    // While Loading, draw nothing: the system splash screen (MainActivity) is still visible.
    if (sessionState == SessionState.Loading) return

    // Decided once, when the session is first known. Later changes are handled by
    // navigation (login) and the auth guard (logout), not by rebuilding the graph.
    val startDestination: Route = remember {
        if (sessionState == SessionState.Authenticated) Route.Home else Route.Welcome
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(250)) +
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(250)) +
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200))
        }
    ) {
        composable<Route.Welcome> {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Route.Login) },
                onNavigateToRegister = { navController.navigate(Route.Register) }
            )
        }

        composable<Route.Register> {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Register) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Login> {
            LoginScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToForgotPassword = { navController.navigate(Route.ForgotPassword) },
                onNavigateToRegister = { navController.navigate(Route.Register) },
                onNavigateToHome = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.ForgotPassword> {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.ForgotPassword) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Home> {
            // Logout navigation is handled by the auth guard above.
            HomeScreen(
                onNavigateToProfile = { navController.navigate(Route.Profile) }
            )
        }

        composable<Route.Profile> {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

/** Screens that require a logged-in user. */
private val PROTECTED_ROUTES = listOf(Route.Home::class, Route.Profile::class)

/** Go to Welcome and clear the whole back stack, so Back can't return to a protected screen. */
private fun NavHostController.navigateToWelcome() {
    navigate(Route.Welcome) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}
