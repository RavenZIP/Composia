package io.github.ravenzip.composia.control.multiValueControl

import io.github.ravenzip.composia.control.shared.ValueChangeEvent
import io.github.ravenzip.composia.control.statusControl.AbstractStatusControl
import io.github.ravenzip.composia.extension.stateInDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

abstract class AbstractMultiValueControl<T, K>(
    initialValue: List<T>,
    val keySelector: (T) -> K,
    private val resetValue: List<T> = emptyList(),
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) : AbstractStatusControl(disabled = disabled, coroutineScope = coroutineScope) {
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

    fun toggle(value: T) {
        _valueFlow.update { current ->
            val currentValues = current.value.toMutableList()
            val valueKey = keySelector(value)
            val existingIndex = currentValues.indexOfFirst { keySelector(it) == valueKey }
            val isExists = existingIndex >= 0

            if (isExists) {
                currentValues.removeAt(existingIndex)
            } else {
                currentValues.add(value)
            }

            ValueChangeEvent.createSetChange(currentValues)
        }
    }

    fun setValue(vararg value: T) {
        _valueFlow.update { ValueChangeEvent.createSetChange(value.toList()) }
    }

    fun setValue(value: List<T>) {
        _valueFlow.update { ValueChangeEvent.createSetChange(value) }
    }

    override fun reset() {
        reset(resetValue)
    }

    fun reset(value: List<T>) {
        super.reset()
        _valueFlow.update { ValueChangeEvent.createResetChange(value) }
    }

    fun reset(vararg value: T) {
        super.reset()
        _valueFlow.update { ValueChangeEvent.createResetChange(value.toList()) }
    }
}
