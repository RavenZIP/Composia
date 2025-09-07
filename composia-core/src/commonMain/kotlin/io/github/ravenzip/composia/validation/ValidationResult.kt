package io.github.ravenzip.composia.validation

sealed class ValidationResult {
    object Valid : ValidationResult()

    class Invalid(val errorMessage: String) : ValidationResult()
}

fun ValidationResult.isValid(): Boolean = this is ValidationResult.Valid

fun ValidationResult.isInvalid(): Boolean = this is ValidationResult.Invalid

fun ValidationResult.getErrorMessage(): String? =
    if (this is ValidationResult.Invalid) this.errorMessage else null
