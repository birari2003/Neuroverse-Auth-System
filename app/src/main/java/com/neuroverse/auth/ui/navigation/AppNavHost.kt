package com.neuroverse.auth.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neuroverse.auth.ui.features.forgotpassword.ForgotPasswordScreen
import com.neuroverse.auth.ui.features.home.HomeScreen
import com.neuroverse.auth.ui.features.login.LoginScreen
import com.neuroverse.auth.ui.features.profile.ProfileScreen
import com.neuroverse.auth.ui.features.register.RegisterScreen
import com.neuroverse.auth.ui.features.splash.SplashScreen
import com.neuroverse.auth.ui.features.splash.SplashViewModel
import com.neuroverse.auth.ui.features.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash,
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
        composable<Route.Splash> {
            val viewModel: SplashViewModel = hiltViewModel()
            val session by viewModel.authState.collectAsStateWithLifecycle()

            SplashScreen(
                session = session,
                onNavigateToHome = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                },
                onNavigateToWelcome = {
                    navController.navigate(Route.Welcome) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                }
            )
        }

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
            HomeScreen(
                onNavigateToProfile = { navController.navigate(Route.Profile) },
                onLoggedOut = {
                    navController.navigate(Route.Welcome) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Profile> {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate(Route.Welcome) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
