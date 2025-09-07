package io.github.ravenzip.composia.state

sealed class ControlState {
    object Disabled : ControlState()

    data class Invalid(val message: String) : ControlState()

    object Valid : ControlState()
}

fun ControlState.toActivationState() =
    when (this) {
        is ControlState.Disabled -> ActivationState.Disabled
        else -> ActivationState.Enabled
    }

fun ControlState.isEnabled() = this !is ControlState.Disabled

fun ControlState.isDisabled() = this is ControlState.Disabled

fun ControlState.isInvalid() = this is ControlState.Invalid

fun ControlState.isValid() = this is ControlState.Valid
