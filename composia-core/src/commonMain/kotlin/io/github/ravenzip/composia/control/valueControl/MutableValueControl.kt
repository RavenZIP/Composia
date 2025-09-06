package io.github.ravenzip.composia.control.valueControl

import io.github.ravenzip.composia.control.enabledControl.EnablementControlSnapshot
import io.github.ravenzip.composia.control.enabledControl.MutableEnablementControlImpl
import io.github.ravenzip.composia.control.shared.ValueChangeEvent
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.status.EnablementState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

interface MutableValueControl<T> : ValueControl<T> {
    fun setValue(value: T)
}

internal class MutableValueControlImpl<T>(
    val initialValue: T,
    val resetValue: T = initialValue,
    enabled: Boolean = true,
) : MutableEnablementControlImpl(enabled), MutableValueControl<T> {
    private val _valueChangeEvents: MutableStateFlow<ValueChangeEvent<T>> =
        MutableStateFlow(ValueChangeEvent.createInitializeChange(initialValue))

    override val valueChangeEvents: StateFlow<ValueChangeEvent<T>> =
        _valueChangeEvents.asStateFlow()

    override val currentValueChangeEvent: ValueChangeEvent<T>
        get() = _valueChangeEvents.value

    override val currentValue: T
        get() = _valueChangeEvents.value.value

    override val currentTypeChange: ValueChangeType
        get() = _valueChangeEvents.value.typeChange

    override fun setValue(value: T) {
        _valueChangeEvents.update { ValueChangeEvent.createSetChange(value) }
    }

    override fun reset() {
        reset(resetValue)
    }

    fun reset(value: T) {
        super.reset()
        _valueChangeEvents.update { ValueChangeEvent.createResetChange(value) }
    }
}

fun <T> mutableValueControlOf(
    initialValue: T,
    resetValue: T = initialValue,
    enabled: Boolean,
): MutableValueControl<T> = MutableValueControlImpl(initialValue, resetValue, enabled)

fun <T> ValueControl<T>.createSnapshotStateFlow(
    initialValue: T,
    initialState: EnablementState,
    coroutineScope: CoroutineScope,
): StateFlow<EnablementControlSnapshot> =
    combine(this.valueChangeEvents, this.enablementFlow) { valueWithTypeChanges, enablementState ->
            ValueControlSnapshotImpl.create(
                valueWithTypeChanges = valueWithTypeChanges,
                state = enablementState,
            )
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ValueControlSnapshotImpl.create(initialValue, initialState),
        )

fun <T> ValueControl<T>.createValueStateFlow(
    initialValue: T,
    coroutineScope: CoroutineScope,
): StateFlow<T> =
    this.valueChangeEvents
        .map { x -> x.value }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialValue,
        )

fun <T> ValueControl<T>.createTypeChangeStateFlow(
    initialTypeChange: ValueChangeType = ValueChangeType.Initialize,
    coroutineScope: CoroutineScope,
): StateFlow<ValueChangeType> =
    this.valueChangeEvents
        .map { x -> x.typeChange }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialTypeChange,
        )
