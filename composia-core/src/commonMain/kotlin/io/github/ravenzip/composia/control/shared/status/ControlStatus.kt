package io.github.ravenzip.composia.control.shared.status

sealed class ControlStatus {
    object Disabled : ControlStatus()

    data class Invalid(val message: String) : ControlStatus()

    object Valid : ControlStatus()
}

fun ControlStatus.toEnablementState() =
    when (this) {
        is ControlStatus.Disabled -> EnablementState.Disabled
        else -> EnablementState.Enabled
    }

fun ControlStatus.isEnabled() = this !is ControlStatus.Disabled

fun ControlStatus.isDisabled() = this is ControlStatus.Disabled

fun ControlStatus.isInvalid() = this is ControlStatus.Invalid

fun ControlStatus.isValid() = this is ControlStatus.Valid
