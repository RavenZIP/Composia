package io.github.ravenzip.composia.control.singleValueControl

import io.github.ravenzip.composia.control.shared.ValueChangeEvent
import io.github.ravenzip.composia.control.statusControl.AbstractStatusControl
import io.github.ravenzip.composia.extension.stateInDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

abstract class AbstractSingleValueControl<T>(
    private val initialValue: T,
    private val resetValue: T = initialValue,
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) : AbstractStatusControl(disabled, coroutineScope) {
    private val _valueFlow = MutableStateFlow(ValueChangeEvent.createInitializeChange(initialValue))

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
        _valueFlow.update { ValueChangeEvent.createSetChange(value) }
    }

    override fun reset() {
        reset(resetValue)
    }

    fun reset(value: T) {
        super.reset()
        _valueFlow.update { ValueChangeEvent.createResetChange(value) }
    }
}
