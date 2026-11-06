package com.neuroverse.auth.domain.validation

import com.neuroverse.auth.core.common.UiText

/**
 * Sealed representation of field validation results.
 */
sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val error: UiText) : ValidationResult

    val isValid: Boolean
        get() = this is Valid
}
