package com.neuroverse.auth.ui.features.register

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is RegisterUiEffect.NavigateToHome -> onNavigateToHome()
                // Toast (not a screen snackbar) so the message stays visible while we navigate to Home.
                is RegisterUiEffect.ShowSuccessMessage -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    RegisterContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    AuthScreenScaffold(
        onNavigateBack = onNavigateBack
    ) {
        AuthHeader(
            title = stringResource(R.string.register_title),
            subtitle = stringResource(R.string.register_subtitle)
        )

        Spacer(modifier = Modifier.height(Dimens.lg))

        ErrorBanner(message = uiState.formError?.asString())

        Spacer(modifier = Modifier.height(Dimens.md))

        // Full Name
        AppTextField(
            value = uiState.name,
            onValueChange = { onEvent(RegisterUiEvent.NameChanged(it)) },
            label = stringResource(R.string.field_full_name),
            leadingIcon = Icons.Outlined.Person,
            errorText = uiState.nameError?.asString(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(Dimens.md))

        // Email
        AppTextField(
            value = uiState.email,
            onValueChange = { onEvent(RegisterUiEvent.EmailChanged(it)) },
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
            onValueChange = { onEvent(RegisterUiEvent.PasswordChanged(it)) },
            label = stringResource(R.string.field_password),
            errorText = uiState.passwordError?.asString(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        // Password rule hint / helper text
        Text(
            text = stringResource(R.string.password_rules_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.md, top = Dimens.xxs)
        )

        Spacer(modifier = Modifier.height(Dimens.md))

        // Confirm Password
        PasswordTextField(
            value = uiState.confirmPassword,
            onValueChange = { onEvent(RegisterUiEvent.ConfirmPasswordChanged(it)) },
            label = stringResource(R.string.field_confirm_password),
            errorText = uiState.confirmPasswordError?.asString(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(RegisterUiEvent.Submit)
                }
            )
        )

        Spacer(modifier = Modifier.height(Dimens.xl))

        PrimaryButton(
            text = stringResource(R.string.action_create_account),
            onClick = {
                focusManager.clearFocus()
                onEvent(RegisterUiEvent.Submit)
            },
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(Dimens.lg))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppTextButton(
                text = stringResource(R.string.action_login),
                onClick = onNavigateToLogin
            )
        }
    }
}

@Preview(name = "RegisterScreen Light", showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    AuthAppTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onEvent = {},
            onNavigateBack = {},
            onNavigateToLogin = {}
        )
    }
}
