package io.github.ravenzip.composia.control.valueControl

import io.github.ravenzip.composia.control.enabledControl.EnablementControlSnapshot
import io.github.ravenzip.composia.control.enabledControl.MutableEnablementControl
import io.github.ravenzip.composia.control.enabledControl.MutableEnablementControlImpl
import io.github.ravenzip.composia.control.shared.ValueChangeEvent
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.status.EnablementState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface MutableValueControl<T> : ValueControl<T>, MutableEnablementControl {
    fun setValue(value: T)

    fun reset(value: T)
}

internal open class MutableValueControlImpl<T>(
    initialValue: T,
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

    override fun reset(value: T) {
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
    combine(valueChangeEvents, enablementFlow) { valueWithTypeChanges, enablementState ->
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
    valueChangeEvents
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
    valueChangeEvents
        .map { x -> x.typeChange }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialTypeChange,
        )

fun <T, K> MutableValueControl<List<T>>.toggle(value: T, keySelector: (T) -> K) {
    val currentValues = currentValue.toMutableList()
    val valueKey = keySelector(value)
    val existingIndex = currentValues.indexOfFirst { keySelector(it) == valueKey }
    val isExists = existingIndex >= 0

    if (isExists) {
        currentValues.removeAt(existingIndex)
    } else {
        currentValues.add(value)
    }

    setValue(currentValues)
}

fun <T> MutableValueControl<List<T>>.setValue(vararg values: T) {
    setValue(values.toList())
}

fun <T> MutableValueControl<List<T>>.reset(vararg values: T) {
    reset(values.toList())
}
