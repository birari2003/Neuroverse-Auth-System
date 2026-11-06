package com.neuroverse.auth.domain.validation

import com.neuroverse.auth.R
import com.neuroverse.auth.core.common.UiText
import javax.inject.Inject

class ValidateName @Inject constructor() {
    operator fun invoke(name: String): ValidationResult {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_name_empty))
        }
        if (trimmed.length < 2) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_name_too_short))
        }
        if (trimmed.length > 50) {
            return ValidationResult.Invalid(UiText.StringResource(R.string.error_name_too_long))
        }
        return ValidationResult.Valid
    }
}
