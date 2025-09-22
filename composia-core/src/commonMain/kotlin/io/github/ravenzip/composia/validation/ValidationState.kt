package io.github.ravenzip.composia.validation

import io.github.ravenzip.composia.status.ControlStatus

sealed class ValidationState {
    object Valid : ValidationState()

    class Invalid(val errorMessage: String) : ValidationState()
}

fun ValidationState.isValid(): Boolean = this is ValidationState.Valid

fun ValidationState.isInvalid(): Boolean = this is ValidationState.Invalid

fun ValidationState.getErrorMessage(): String? =
    if (this is ValidationState.Invalid) this.errorMessage else null

fun ValidationState.toControlStatus() =
    when (this) {
        ValidationState.Valid -> ControlStatus.Valid
        is ValidationState.Invalid -> ControlStatus.Invalid(this.errorMessage)
    }
