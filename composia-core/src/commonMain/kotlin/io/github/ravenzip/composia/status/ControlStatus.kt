package io.github.ravenzip.composia.status

sealed class ControlStatus {
    object Disabled : ControlStatus()

    data class Invalid(val message: String) : ControlStatus()

    object Valid : ControlStatus()
}

fun ControlStatus.isEnabled(): Boolean = this !is ControlStatus.Disabled

fun ControlStatus.isDisabled(): Boolean = this is ControlStatus.Disabled

fun ControlStatus.isInvalid(): Boolean = this is ControlStatus.Invalid

fun ControlStatus.isValid(): Boolean = this is ControlStatus.Valid
