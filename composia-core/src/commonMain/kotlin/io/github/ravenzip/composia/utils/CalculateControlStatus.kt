package io.github.ravenzip.composia.utils

import io.github.ravenzip.composia.control.shared.status.ControlStatus

internal fun calculateControlStatus(errorMessage: String = "") =
    if (errorMessage.isNotEmpty()) ControlStatus.Invalid(errorMessage) else ControlStatus.Valid

internal fun calculateControlStatus(isDisabled: Boolean = false) =
    if (isDisabled) ControlStatus.Disabled else ControlStatus.Valid
