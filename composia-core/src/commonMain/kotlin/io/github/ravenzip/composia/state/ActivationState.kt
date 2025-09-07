package io.github.ravenzip.composia.state

sealed interface ActivationState {
    object Enabled : ActivationState

    object Disabled : ActivationState
}

fun activationStatusOf(value: Boolean): ActivationState =
    if (value) ActivationState.Enabled else ActivationState.Disabled

fun ActivationState.toControlState(errorMessage: String): ControlState =
    when {
        this is ActivationState.Disabled -> ControlState.Disabled
        errorMessage.isNotEmpty() -> ControlState.Invalid(errorMessage)
        else -> ControlState.Valid
    }

fun ActivationState.isEnabled(): Boolean = this is ActivationState.Enabled

fun ActivationState.isDisabled(): Boolean = this is ActivationState.Disabled
