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
    val valueChangeFlow: StateFlow<ValueChange<T>>
    val valueFlow: StateFlow<T>
    val typeChangeFlow: StateFlow<ValueChangeType>
    val valueChange: ValueChange<T>
    val value: T
    val typeChange: ValueChangeType
    val defaultResetValue: T
    val snapshotFlow: StateFlow<ValueControlSnapshot<T>>
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
    private val _valueChangeFlow: MutableStateFlow<ValueChange<T>> =
        MutableStateFlow(ValueChange.createInitializeChange(initialValue))

    override val valueChangeFlow: StateFlow<ValueChange<T>> = _valueChangeFlow.asStateFlow()

    override val valueFlow: StateFlow<T> =
        _valueChangeFlow
            .map { event -> event.value }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = initialValue)

    override val typeChangeFlow: StateFlow<ValueChangeType> =
        valueChangeFlow
            .map { x -> x.typeChange }
            .stateInWhileSubscribed(
                scope = coroutineScope,
                initialValue = ValueChangeType.Initialize,
            )

    override val valueChange: ValueChange<T>
        get() = _valueChangeFlow.value

    override val value: T
        get() = _valueChangeFlow.value.value

    override val typeChange: ValueChangeType
        get() = _valueChangeFlow.value.typeChange

    override val defaultResetValue: T = resetValue

    override val snapshotFlow: StateFlow<ValueControlSnapshot<T>> =
        combine(valueChangeFlow, activationStateFlow) { valueWithTypeChanges, enablementState ->
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
        _valueChangeFlow.update { ValueChange.createSetChange(value) }
    }

    override fun reset() {
        reset(resetValue)
    }

    override fun reset(value: T) {
        super.reset()
        _valueChangeFlow.update { ValueChange.createResetChange(value) }
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
    val currentValues = this.value.addOrRemove(value, keySelector)

    setValue(currentValues)
}

fun <T> MutableValueControl<List<T>>.setValue(vararg values: T) {
    setValue(values.toList())
}

fun <T> MutableValueControl<List<T>>.reset(vararg values: T) {
    reset(values.toList())
}
