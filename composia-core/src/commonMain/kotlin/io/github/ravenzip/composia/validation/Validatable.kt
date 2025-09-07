package io.github.ravenzip.composia.validation

import kotlinx.coroutines.flow.StateFlow

interface Validatable<T> {
    val validationResultState: StateFlow<ValidationResult>
    val currentErrorMessage: String?
    val isValid: Boolean
    val isInvalid: Boolean

    fun validate(value: T)
}
