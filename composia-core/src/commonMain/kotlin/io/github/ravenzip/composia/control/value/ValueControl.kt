package io.github.ravenzip.composia.control.value

import io.github.ravenzip.composia.control.activation.ActivationControl
import io.github.ravenzip.composia.control.activation.MutableActivationControl
import io.github.ravenzip.composia.control.activation.MutableActivationControlImpl
import io.github.ravenzip.composia.extension.addOrRemove
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.valueChange.ValueChange
import io.github.ravenzip.composia.valueChange.ValueChangeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface ValueControl<T> : ActivationControl {
    val valueChangeState: StateFlow<ValueChange<T>>
    val valueState: StateFlow<T>
    val typeChangeState: StateFlow<ValueChangeType>
    val currentValueChange: ValueChange<T>
    val currentValue: T
    val currentTypeChange: ValueChangeType
    val defaultResetValue: T
    val snapshotState: StateFlow<ValueControlSnapshot<T>>
}

interface MutableValueControl<T> : ValueControl<T>, MutableActivationControl {
    fun setValue(value: T)

    fun reset(value: T)
}

internal open class MutableValueControlImpl<T>(
    initialValue: T,
    private val resetValue: T = initialValue,
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
) : MutableActivationControlImpl(enabled, coroutineScope), MutableValueControl<T> {
    private val _valueChangeState: MutableStateFlow<ValueChange<T>> =
        MutableStateFlow(ValueChange.createInitializeChange(initialValue))

    override val valueChangeState: StateFlow<ValueChange<T>> = _valueChangeState.asStateFlow()

    override val valueState: StateFlow<T> =
        _valueChangeState
            .map { event -> event.value }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = initialValue)

    override val typeChangeState: StateFlow<ValueChangeType> =
        valueChangeState
            .map { x -> x.typeChange }
            .stateInWhileSubscribed(
                scope = coroutineScope,
                initialValue = ValueChangeType.Initialize,
            )

    override val currentValueChange: ValueChange<T>
        get() = _valueChangeState.value

    override val currentValue: T
        get() = _valueChangeState.value.value

    override val currentTypeChange: ValueChangeType
        get() = _valueChangeState.value.typeChange

    override val defaultResetValue: T = resetValue

    override val snapshotState: StateFlow<ValueControlSnapshot<T>> =
        combine(valueChangeState, activationState) { valueWithTypeChanges, enablementState ->
                ValueControlSnapshotImpl.create(
                    valueChange = valueWithTypeChanges,
                    state = enablementState,
                )
            }
            .stateInWhileSubscribed(
                scope = coroutineScope,
                initialValue = ValueControlSnapshotImpl.create(initialValue, initialState),
            )

    override fun setValue(value: T) {
        _valueChangeState.update { ValueChange.createSetChange(value) }
    }

    override fun reset() {
        reset(resetValue)
    }

    override fun reset(value: T) {
        super.reset()
        _valueChangeState.update { ValueChange.createResetChange(value) }
    }
}

fun <T> mutableValueControlOf(
    initialValue: T,
    resetValue: T = initialValue,
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableValueControl<T> =
    MutableValueControlImpl(initialValue, resetValue, enabled, coroutineScope)

fun <T> MutableValueControl<T>.asReadonly(): ValueControl<T> = this

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
