package io.github.ravenzip.composia.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Базовый абстрактный класс для хранения и управления булевым состоянием, с возможностью
 * реактивного отслеживания изменений
 *
 * @property initialValue начальное значение состояния
 */
abstract class BooleanState(val initialValue: Boolean = false) {
    private val _value: MutableStateFlow<Boolean> = MutableStateFlow(initialValue)

    val valueChanges: StateFlow<Boolean> = _value.asStateFlow()

    val currentValue: Boolean
        get() = _value.value

    fun setValue(newValue: Boolean) {
        _value.value = newValue
    }

    fun toggle() = setValue(!_value.value)
}
