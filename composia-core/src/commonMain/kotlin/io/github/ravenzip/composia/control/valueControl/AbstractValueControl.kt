package io.github.ravenzip.composia.control.valueControl

import io.github.ravenzip.composia.control.shared.ValueWithTypeChanges
import io.github.ravenzip.composia.control.statusControl.AbstractStatusControl
import io.github.ravenzip.composia.extension.stateInDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

abstract class AbstractValueControl<T>(
    private val initialValue: T,
    private val resetValue: T = initialValue,
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) : AbstractStatusControl(disabled, coroutineScope) {
    private val _valueFlow =
        MutableStateFlow(ValueWithTypeChanges.createInitializeChange(initialValue))

    val valueFlow =
        _valueFlow
            .map { x -> x.value }
            .stateInDefault(scope = coroutineScope, initialValue = initialValue)

    val valueWithTypeChangesFlow = _valueFlow.asStateFlow()

    val value
        get() = _valueFlow.value.value

    val valueWithTypeChanges
        get() = _valueFlow.value

    fun setValue(value: T) {
        _valueFlow.update { ValueWithTypeChanges.createSetChange(value) }
    }

    override fun reset() {
        reset(resetValue)
    }

    fun reset(value: T) {
        super.reset()
        _valueFlow.update { ValueWithTypeChanges.createResetChange(value) }
    }
}
