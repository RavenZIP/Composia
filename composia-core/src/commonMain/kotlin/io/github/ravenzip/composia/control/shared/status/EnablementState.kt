package io.github.ravenzip.composia.control.shared.status

sealed interface EnablementState {
    object Enabled : EnablementState

    object Disabled : EnablementState
}

fun enablementStatusOf(value: Boolean) =
    if (value) EnablementState.Enabled else EnablementState.Disabled

fun EnablementState.toControlState(errorMessage: String) =
    when {
        this is EnablementState.Disabled -> ControlStatus.Disabled
        errorMessage.isNotEmpty() -> ControlStatus.Invalid(errorMessage)
        else -> ControlStatus.Valid
    }

fun EnablementState.isEnabled() = this is EnablementState.Enabled

fun EnablementState.isDisabled() = this is EnablementState.Disabled
