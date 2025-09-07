package io.github.ravenzip.composia.control.value

import io.github.ravenzip.composia.control.activation.ActivationControl
import io.github.ravenzip.composia.control.activation.MutableActivationControl
import io.github.ravenzip.composia.control.activation.MutableActivationControlImpl
import io.github.ravenzip.composia.extension.addOrRemove
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.valueChange.ValueChangeEvent
import io.github.ravenzip.composia.valueChange.ValueChangeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface ValueControl<T> : ActivationControl {
    val valueChangeEvents: StateFlow<ValueChangeEvent<T>>
    val valueEvents: StateFlow<T>
    val typeChangesEvents: StateFlow<ValueChangeType>
    val currentValueChangeEvent: ValueChangeEvent<T>
    val currentValue: T
    val currentTypeChange: ValueChangeType
    val defaultResetValue: T
    val snapshotEvents: StateFlow<ValueControlSnapshot<T>>
}

interface MutableValueControl<T> : ValueControl<T>, MutableActivationControl {
    fun setValue(value: T)

    fun reset(value: T)
}

internal open class MutableValueControlImpl<T>(
    initialValue: T,
    val resetValue: T = initialValue,
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
) : MutableActivationControlImpl(enabled, coroutineScope), MutableValueControl<T> {
    private val _valueChangeEvents: MutableStateFlow<ValueChangeEvent<T>> =
        MutableStateFlow(ValueChangeEvent.createInitializeChange(initialValue))

    override val valueChangeEvents: StateFlow<ValueChangeEvent<T>> =
        _valueChangeEvents.asStateFlow()

    override val valueEvents: StateFlow<T> =
        _valueChangeEvents
            .map { event -> event.value }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = initialValue)

    override val typeChangesEvents: StateFlow<ValueChangeType> =
        valueChangeEvents
            .map { x -> x.typeChange }
            .stateInWhileSubscribed(
                scope = coroutineScope,
                initialValue = ValueChangeType.Initialize,
            )

    override val currentValueChangeEvent: ValueChangeEvent<T>
        get() = _valueChangeEvents.value

    override val currentValue: T
        get() = _valueChangeEvents.value.value

    override val currentTypeChange: ValueChangeType
        get() = _valueChangeEvents.value.typeChange

    override val defaultResetValue: T
        get() = resetValue

    override val snapshotEvents: StateFlow<ValueControlSnapshot<T>> =
        combine(valueChangeEvents, activationStateEvents) { valueWithTypeChanges, enablementState ->
                ValueControlSnapshotImpl.create(
                    valueChangeEvent = valueWithTypeChanges,
                    state = enablementState,
                )
            }
            .stateInWhileSubscribed(
                scope = coroutineScope,
                initialValue = ValueControlSnapshotImpl.create(initialValue, initialState),
            )

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
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableValueControl<T> =
    MutableValueControlImpl(initialValue, resetValue, enabled, coroutineScope)

fun <T, K> MutableValueControl<List<T>>.toggle(value: T, keySelector: (T) -> K) {
    val currentValues = currentValue.addOrRemove(value, keySelector)

    setValue(currentValues)
}

fun <T> MutableValueControl<List<T>>.setValue(vararg values: T) {
    setValue(values.toList())
}

fun <T> MutableValueControl<List<T>>.reset(vararg values: T) {
    reset(values.toList())
}
