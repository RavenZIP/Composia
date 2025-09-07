package io.github.ravenzip.composia.control.shared.status

sealed interface ActivationState {
    object Enabled : ActivationState

    object Disabled : ActivationState
}

fun activationStatusOf(value: Boolean) =
    if (value) ActivationState.Enabled else ActivationState.Disabled

fun ActivationState.toControlState(errorMessage: String) =
    when {
        this is ActivationState.Disabled -> ControlState.Disabled
        errorMessage.isNotEmpty() -> ControlState.Invalid(errorMessage)
        else -> ControlState.Valid
    }

fun ActivationState.isEnabled() = this is ActivationState.Enabled

fun ActivationState.isDisabled() = this is ActivationState.Disabled
