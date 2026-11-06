package com.neuroverse.auth.ui.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neuroverse.auth.R
import com.neuroverse.auth.domain.model.User
import com.neuroverse.auth.ui.features.account.AccountUiEvent
import com.neuroverse.auth.ui.features.account.AccountUiState
import com.neuroverse.auth.ui.features.account.AccountViewModel
import com.neuroverse.auth.ui.components.buttons.PrimaryButton
import com.neuroverse.auth.ui.components.data.InfoRow
import com.neuroverse.auth.ui.components.data.ProfileAvatar
import com.neuroverse.auth.ui.components.dialogs.ConfirmDialog
import com.neuroverse.auth.ui.components.layout.AppTopBar
import com.neuroverse.auth.ui.theme.AuthAppTheme
import com.neuroverse.auth.ui.theme.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun ProfileContent(
    uiState: AccountUiState,
    onEvent: (AccountUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val user = uiState.user
    val memberSince = user?.let {
        SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(it.createdAtMillis))
    } ?: "Recent"

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_profile),
                onNavigateBack = onNavigateBack
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 96dp Avatar
                ProfileAvatar(
                    name = user?.name ?: "User",
                    size = Dimens.avatarProfile
                )

                Spacer(modifier = Modifier.height(Dimens.md))

                Text(
                    text = user?.name ?: stringResource(R.string.default_user_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(Dimens.xxs))

                Text(
                    text = user?.email ?: "user@neuroverse.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(Dimens.xl))

                // User details card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(Dimens.md)) {
                        InfoRow(
                            icon = Icons.Outlined.Person,
                            label = stringResource(R.string.field_full_name),
                            value = user?.name ?: "-"
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = Dimens.xs),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                        InfoRow(
                            icon = Icons.Outlined.Email,
                            label = stringResource(R.string.field_email),
                            value = user?.email ?: "-"
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = Dimens.xs),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                        InfoRow(
                            icon = Icons.Outlined.CalendarToday,
                            label = stringResource(R.string.profile_member_since),
                            value = memberSince
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = Dimens.xs),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                        InfoRow(
                            icon = Icons.Outlined.Fingerprint,
                            label = stringResource(R.string.profile_user_id),
                            value = user?.id ?: "-"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.xxl))

            // Reused logout action and confirm dialog
            PrimaryButton(
                text = stringResource(R.string.action_logout),
                onClick = { onEvent(AccountUiEvent.RequestLogout) },
                isLoading = uiState.isLoading
            )
        }
    }

    if (uiState.showLogoutConfirmDialog) {
        ConfirmDialog(
            title = stringResource(R.string.dialog_logout_title),
            message = stringResource(R.string.dialog_logout_message),
            confirmText = stringResource(R.string.action_logout),
            onConfirm = { onEvent(AccountUiEvent.ConfirmLogout) },
            onDismiss = { onEvent(AccountUiEvent.DismissLogoutDialog) }
        )
    }
}

@Preview(name = "ProfileScreen Light", showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    AuthAppTheme {
        ProfileContent(
            uiState = AccountUiState(
                user = User("usr_demo", "Alex Mercer", "alex@neuroverse.com", 1700000000000L)
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
