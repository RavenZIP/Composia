package io.github.ravenzip.composia.control.valueControl

import io.github.ravenzip.composia.control.enabledControl.EnablementControl
import io.github.ravenzip.composia.control.shared.ValueChangeEvent
import io.github.ravenzip.composia.control.shared.ValueChangeType
import kotlinx.coroutines.flow.StateFlow

interface ValueControl<T> : EnablementControl {
    val valueChangeEvents: StateFlow<ValueChangeEvent<T>>
    val currentValueChangeEvent: ValueChangeEvent<T>
    val currentValue: T
    val currentTypeChange: ValueChangeType
}
