package io.github.ravenzip.composia.control.statusControl

import io.github.ravenzip.composia.control.extension.stateInDefault
import io.github.ravenzip.composia.control.shared.ControlStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

abstract class AbstractStatusControl(
    private val disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) {
    private val _statusFlow =
        MutableStateFlow(if (disabled) ControlStatus.Disabled else ControlStatus.Valid)

    open val statusFlow =
        _statusFlow.stateInDefault(scope = coroutineScope, initialValue = ControlStatus.Valid)

    open val status
        get() = _statusFlow.value

    val isDisabledFlow =
        _statusFlow
            .map { value -> value is ControlStatus.Disabled }
            .stateInDefault(scope = coroutineScope, initialValue = false)

    val isDisabled
        get() = isDisabledFlow.value

    val isEnabledFlow =
        _statusFlow
            .map { value -> value !is ControlStatus.Disabled }
            .stateInDefault(scope = coroutineScope, initialValue = true)

    val isEnabled
        get() = isEnabledFlow.value

    fun disable() {
        _statusFlow.update { ControlStatus.Disabled }
    }

    fun enable() {
        _statusFlow.update { ControlStatus.Valid }
    }

    open fun reset() {
        _statusFlow.update { if (disabled) ControlStatus.Disabled else ControlStatus.Valid }
    }
}
