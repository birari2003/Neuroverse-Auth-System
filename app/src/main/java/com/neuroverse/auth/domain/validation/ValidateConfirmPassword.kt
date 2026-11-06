package com.neuroverse.auth.domain.validation

import com.neuroverse.auth.R
import com.neuroverse.auth.core.common.UiText
import javax.inject.Inject

class ValidateConfirmPassword @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (confirmPassword.isEmpty()) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_confirm_password_empty))
        }
        if (password != confirmPassword) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_passwords_do_not_match))
        }
        return ValidationResult.Valid
    }
}
