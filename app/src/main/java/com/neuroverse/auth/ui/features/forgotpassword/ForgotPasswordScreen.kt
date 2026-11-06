package com.neuroverse.auth.ui.features.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neuroverse.auth.R
import com.neuroverse.auth.ui.components.buttons.PrimaryButton
import com.neuroverse.auth.ui.components.feedback.ErrorBanner
import com.neuroverse.auth.ui.components.inputs.AppTextField
import com.neuroverse.auth.ui.components.layout.AuthHeader
import com.neuroverse.auth.ui.components.layout.AuthScreenScaffold
import com.neuroverse.auth.ui.theme.AuthAppTheme
import com.neuroverse.auth.ui.theme.Dimens
import com.neuroverse.auth.ui.theme.SuccessColor

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ForgotPasswordContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun ForgotPasswordContent(
    uiState: ForgotPasswordUiState,
    onEvent: (ForgotPasswordUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    AuthScreenScaffold(
        onNavigateBack = onNavigateBack
    ) {
        if (uiState.isSuccess) {
            // Success confirmation state
            Spacer(modifier = Modifier.height(Dimens.xxl))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(SuccessColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.lg))

            Text(
                text = stringResource(R.string.reset_success_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.xs))

            Text(
                text = stringResource(R.string.reset_success_subtitle, uiState.submittedEmail),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.xxl))

            PrimaryButton(
                text = stringResource(R.string.action_back_to_login),
                onClick = onNavigateToLogin
            )
        } else {
            // Form state
            AuthHeader(
                title = stringResource(R.string.forgot_password_title),
                subtitle = stringResource(R.string.forgot_password_subtitle)
            )

            Spacer(modifier = Modifier.height(Dimens.lg))

            ErrorBanner(message = uiState.formError?.asString())

            Spacer(modifier = Modifier.height(Dimens.md))

            AppTextField(
                value = uiState.email,
                onValueChange = { onEvent(ForgotPasswordUiEvent.EmailChanged(it)) },
                label = stringResource(R.string.field_email),
                leadingIcon = Icons.Outlined.Email,
                errorText = uiState.emailError?.asString(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onEvent(ForgotPasswordUiEvent.Submit)
                    }
                )
            )

            Spacer(modifier = Modifier.height(Dimens.xl))

            PrimaryButton(
                text = stringResource(R.string.action_send_reset_link),
                onClick = {
                    focusManager.clearFocus()
                    onEvent(ForgotPasswordUiEvent.Submit)
                },
                isLoading = uiState.isLoading
            )
        }
    }
}

@Preview(name = "ForgotPassword Form Preview", showBackground = true)
@Composable
private fun ForgotPasswordFormPreview() {
    AuthAppTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(),
            onEvent = {},
            onNavigateBack = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(name = "ForgotPassword Success Preview", showBackground = true)
@Composable
private fun ForgotPasswordSuccessPreview() {
    AuthAppTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(
                isSuccess = true,
                submittedEmail = "alex@neuroverse.com"
            ),
            onEvent = {},
            onNavigateBack = {},
            onNavigateToLogin = {}
        )
    }
}
