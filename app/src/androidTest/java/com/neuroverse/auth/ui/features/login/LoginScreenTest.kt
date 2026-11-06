package com.neuroverse.auth.ui.features.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.neuroverse.auth.ui.theme.AuthAppTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysElementsAndAllowsInput() {
        var submitted = false

        composeTestRule.setContent {
            AuthAppTheme {
                LoginContent(
                    uiState = LoginUiState(),
                    onEvent = { event ->
                        if (event is LoginUiEvent.Submit) submitted = true
                    },
                    onNavigateBack = {},
                    onNavigateToForgotPassword = {},
                    onNavigateToRegister = {}
                )
            }
        }

        // Verify title & buttons are displayed
        composeTestRule.onNodeWithText("Welcome back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Up").assertIsDisplayed()

        // Type email
        composeTestRule.onNodeWithText("Email").performTextInput("alex@neuroverse.com")

        // Click submit
        composeTestRule.onNodeWithText("Log In").performClick()
        assert(submitted)
    }
}
