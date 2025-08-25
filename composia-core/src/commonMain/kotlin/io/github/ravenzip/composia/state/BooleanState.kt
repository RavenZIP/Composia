package io.github.ravenzip.composia.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BooleanState(val initialValue: Boolean = false) {
    private val _value = MutableStateFlow(initialValue)

    val valueFlow = _value.asStateFlow()
    val value
        get() = _value.value

    fun setValue(newValue: Boolean) {
        _value.value = newValue
    }

    fun toggle() = setValue(!_value.value)
}
