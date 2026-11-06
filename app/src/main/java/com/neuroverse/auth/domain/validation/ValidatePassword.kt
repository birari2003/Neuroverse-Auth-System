package com.neuroverse.auth.domain.validation

import com.neuroverse.auth.R
import com.neuroverse.auth.core.common.UiText
import javax.inject.Inject

class ValidatePassword @Inject constructor() {

    /**
     * Strict validation for registration: min 8 chars, 1 uppercase, 1 lowercase, 1 digit.
     */
    fun validateForRegistration(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_password_empty))
        }
        if (password.length < 8) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_password_length))
        }
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        if (!hasUppercase || !hasLowercase || !hasDigit) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_password_complexity))
        }
        return ValidationResult.Valid
    }

    /**
     * Lenient check for login: required only (do not leak policy/hints).
     */
    fun validateForLogin(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_password_empty))
        }
        return ValidationResult.Valid
    }
}
