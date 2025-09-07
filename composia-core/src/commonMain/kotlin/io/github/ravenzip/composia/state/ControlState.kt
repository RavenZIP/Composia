package io.github.ravenzip.composia.state

sealed class ControlState {
    object Disabled : ControlState()

    data class Invalid(val message: String) : ControlState()

    object Valid : ControlState()
}

fun ControlState.toActivationState(): ActivationState =
    when (this) {
        is ControlState.Disabled -> ActivationState.Disabled
        else -> ActivationState.Enabled
    }

fun ControlState.isEnabled(): Boolean = this !is ControlState.Disabled

fun ControlState.isDisabled(): Boolean = this is ControlState.Disabled

fun ControlState.isInvalid(): Boolean = this is ControlState.Invalid

fun ControlState.isValid(): Boolean = this is ControlState.Valid
