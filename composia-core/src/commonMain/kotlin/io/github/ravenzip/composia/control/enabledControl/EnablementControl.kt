package io.github.ravenzip.composia.control.enabledControl

import io.github.ravenzip.composia.control.shared.status.EnablementState
import kotlinx.coroutines.flow.StateFlow

interface EnablementControl {
    val enablementEvents: StateFlow<EnablementState>
    val isEnabled: Boolean
    val isDisabled: Boolean
}
