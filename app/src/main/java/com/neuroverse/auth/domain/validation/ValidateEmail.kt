package com.neuroverse.auth.domain.validation

import com.neuroverse.auth.R
import com.neuroverse.auth.core.common.UiText
import javax.inject.Inject

class ValidateEmail @Inject constructor() {
    // Pure Kotlin regex matching standard RFC email patterns (no android.util.Patterns dependency)
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    operator fun invoke(email: String): ValidationResult {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_email_empty))
        }
        if (!emailRegex.matches(trimmed)) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_email_invalid))
        }
        return ValidationResult.Valid
    }
}
