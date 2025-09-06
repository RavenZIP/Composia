package io.github.ravenzip.composia.control.validation

import kotlinx.coroutines.flow.StateFlow

interface Validatable<T> {
    val validationResultEvents: StateFlow<ValidationResult>
    val currentErrorMessage: String?
    val isValid: Boolean
    val isInvalid: Boolean

    fun validate(value: T)
}
