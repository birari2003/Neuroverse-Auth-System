package com.neuroverse.auth.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neuroverse.auth.R
import com.neuroverse.auth.domain.model.User
import com.neuroverse.auth.ui.components.buttons.SecondaryButton
import com.neuroverse.auth.ui.components.data.FeatureCard
import com.neuroverse.auth.ui.components.data.ProfileAvatar
import com.neuroverse.auth.ui.components.dialogs.ConfirmDialog
import com.neuroverse.auth.ui.components.layout.AppTopBar
import com.neuroverse.auth.ui.theme.AuthAppTheme
import com.neuroverse.auth.ui.theme.Dimens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToWelcome -> onLoggedOut()
            }
        }
    }

    HomeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToProfile = onNavigateToProfile
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val firstName = uiState.user?.name?.split(" ")?.firstOrNull() ?: stringResource(R.string.default_user_name)

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_home),
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = stringResource(R.string.cd_user_profile)
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.screenHorizontalPadding, vertical = Dimens.md),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileAvatar(
                        name = uiState.user?.name ?: "User",
                        size = Dimens.avatarHome
                    )
                    Spacer(modifier = Modifier.width(Dimens.md))
                    Column {
                        Text(
                            text = stringResource(R.string.home_greeting, firstName),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = stringResource(R.string.home_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.xl))

                // 3 Feature Cards
                FeatureCard(
                    icon = Icons.Outlined.Shield,
                    title = stringResource(R.string.feature_secure_sessions_title),
                    description = stringResource(R.string.feature_secure_sessions_desc)
                )

                Spacer(modifier = Modifier.height(Dimens.md))

                FeatureCard(
                    icon = Icons.Outlined.Security,
                    title = stringResource(R.string.feature_protected_routes_title),
                    description = stringResource(R.string.feature_protected_routes_desc)
                )

                Spacer(modifier = Modifier.height(Dimens.md))

                FeatureCard(
                    icon = Icons.Outlined.Key,
                    title = stringResource(R.string.feature_clean_architecture_title),
                    description = stringResource(R.string.feature_clean_architecture_desc)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.xxl))

            // Log Out Button
            SecondaryButton(
                text = stringResource(R.string.action_logout),
                onClick = { onEvent(HomeUiEvent.RequestLogout) },
                isLoading = uiState.isLoading
            )
        }
    }

    if (uiState.showLogoutConfirmDialog) {
        ConfirmDialog(
            title = stringResource(R.string.dialog_logout_title),
            message = stringResource(R.string.dialog_logout_message),
            confirmText = stringResource(R.string.action_logout),
            onConfirm = { onEvent(HomeUiEvent.ConfirmLogout) },
            onDismiss = { onEvent(HomeUiEvent.DismissLogoutDialog) }
        )
    }
}

@Preview(name = "HomeScreen Light", showBackground = true)
@Composable
private fun HomeScreenPreview() {
    AuthAppTheme {
        HomeContent(
            uiState = HomeUiState(
                user = User("1", "Alex Mercer", "alex@neuroverse.com", 1700000000000L)
            ),
            onEvent = {},
            onNavigateToProfile = {}
        )
    }
}
