package io.github.ravenzip.composia.control

import io.github.ravenzip.composia.ControlStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

abstract class AbstractControl(
    private val disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) {
    private val _disabled = MutableStateFlow(disabled)

    private val _status =
        _disabled
            .map { value -> if (value) ControlStatus.Disabled else ControlStatus.Valid }
            .shareIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                replay = 1,
            )

    open val status =
        _status.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ControlStatus.Valid,
        )

    val isDisabled =
        _status
            .map { value -> value is ControlStatus.Disabled }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false,
            )

    val isEnabled =
        _status
            .map { value -> value !is ControlStatus.Disabled }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = true,
            )

    fun disable() {
        _disabled.update { true }
    }

    fun enable() {
        _disabled.update { false }
    }

    open fun reset() {
        _disabled.update { disabled }
    }
}
