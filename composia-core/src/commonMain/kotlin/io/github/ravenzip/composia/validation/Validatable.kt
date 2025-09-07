package io.github.ravenzip.composia.validation

import kotlinx.coroutines.flow.StateFlow

interface Validatable<T> {
    val validationResultFlow: StateFlow<ValidationResult>
    val validationResult: ValidationResult
    val errorMessageFlow: StateFlow<String?>
    val errorMessage: String?
    val isValid: Boolean
    val isInvalid: Boolean

    fun validate(value: T)
}
