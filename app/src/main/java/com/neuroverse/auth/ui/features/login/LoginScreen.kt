package com.neuroverse.auth.ui.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neuroverse.auth.R
import com.neuroverse.auth.ui.components.buttons.AppTextButton
import com.neuroverse.auth.ui.components.buttons.PrimaryButton
import com.neuroverse.auth.ui.components.feedback.ErrorBanner
import com.neuroverse.auth.ui.components.inputs.AppTextField
import com.neuroverse.auth.ui.components.inputs.PasswordTextField
import com.neuroverse.auth.ui.components.layout.AuthHeader
import com.neuroverse.auth.ui.components.layout.AuthScreenScaffold
import com.neuroverse.auth.ui.theme.AuthAppTheme
import com.neuroverse.auth.ui.theme.Dimens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is LoginUiEffect.NavigateToHome -> onNavigateToHome()
                is LoginUiEffect.NavigateToForgotPassword -> onNavigateToForgotPassword()
                is LoginUiEffect.NavigateToRegister -> onNavigateToRegister()
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onNavigateToForgotPassword = onNavigateToForgotPassword,
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    AuthScreenScaffold(
        onNavigateBack = onNavigateBack
    ) {
        AuthHeader(
            title = stringResource(R.string.login_title),
            subtitle = stringResource(R.string.login_subtitle)
        )

        Spacer(modifier = Modifier.height(Dimens.lg))

        ErrorBanner(message = uiState.formError?.asString())

        Spacer(modifier = Modifier.height(Dimens.md))

        // Email
        AppTextField(
            value = uiState.email,
            onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
            label = stringResource(R.string.field_email),
            leadingIcon = Icons.Outlined.Email,
            errorText = uiState.emailError?.asString(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(Dimens.md))

        // Password
        PasswordTextField(
            value = uiState.password,
            onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
            label = stringResource(R.string.field_password),
            errorText = uiState.passwordError?.asString(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(LoginUiEvent.Submit)
                }
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            AppTextButton(
                text = stringResource(R.string.action_forgot_password),
                onClick = onNavigateToForgotPassword
            )
        }

        Spacer(modifier = Modifier.height(Dimens.lg))

        PrimaryButton(
            text = stringResource(R.string.action_login),
            onClick = {
                focusManager.clearFocus()
                onEvent(LoginUiEvent.Submit)
            },
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(Dimens.lg))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dont_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppTextButton(
                text = stringResource(R.string.action_sign_up),
                onClick = onNavigateToRegister
            )
        }
    }
}

@Preview(name = "LoginScreen Light", showBackground = true)
@Composable
private fun LoginScreenPreview() {
    AuthAppTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEvent = {},
            onNavigateBack = {},
            onNavigateToForgotPassword = {},
            onNavigateToRegister = {}
        )
    }
}
