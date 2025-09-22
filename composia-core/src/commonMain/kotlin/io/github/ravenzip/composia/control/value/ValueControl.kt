package io.github.ravenzip.composia.control.value

import androidx.compose.runtime.Stable
import io.github.ravenzip.composia.control.activation.ActivationControl
import io.github.ravenzip.composia.control.activation.MutableActivationControl
import io.github.ravenzip.composia.control.activation.mutableActivationControlOf
import io.github.ravenzip.composia.extension.addOrRemove
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.valueChange.ValueChange
import io.github.ravenzip.composia.valueChange.ValueChangeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

@Stable
interface ValueControl<T> : ActivationControl {
    val valueChangeFlow: StateFlow<ValueChange<T>>
    val valueFlow: StateFlow<T>
    val typeChangeFlow: StateFlow<ValueChangeType>
    val snapshotFlow: StateFlow<ValueControlSnapshot<T>>
    val valueWithTypeChange: ValueChange<T>
    val value: T
    val typeChange: ValueChangeType
    val snapshot: ValueControlSnapshot<T>
}

@Stable
interface MutableValueControl<T> : ValueControl<T>, MutableActivationControl {
    fun setValue(value: T)

    fun reset(value: T)

    fun resetValueOnly()

    fun resetValueOnly(value: T)
}

internal class MutableValueControlImpl<T>(
    private val initialValue: T,
    private val resetValue: T = initialValue,
    private val activationControl: MutableActivationControl,
    coroutineScope: CoroutineScope,
) : MutableValueControl<T>, MutableActivationControl by activationControl {
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

    override val snapshotFlow: StateFlow<ValueControlSnapshot<T>> =
        combine(valueChangeFlow, activationControl.isEnabledFlow) { valueWithTypeChanges, enabled ->
                ValueControlSnapshotImpl.create(
                    valueChange = valueWithTypeChanges,
                    isEnabled = enabled,
                )
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Eagerly,
                initialValue =
                    ValueControlSnapshotImpl.create(initialValue, activationControl.isEnabled),
            )

    override val snapshot: ValueControlSnapshot<T>
        get() = snapshotFlow.value

    override val isEnabled: Boolean
        get() = activationControl.isEnabled

    override val isDisabled: Boolean
        get() = activationControl.isDisabled

    override val valueWithTypeChange: ValueChange<T>
        get() = _valueChangeFlow.value

    override val value: T
        get() = _valueChangeFlow.value.value

    override val typeChange: ValueChangeType
        get() = _valueChangeFlow.value.typeChange

    override fun setValue(value: T) = _valueChangeFlow.update { ValueChange.createSetChange(value) }

    override fun reset() = reset(resetValue)

    override fun reset(value: T) = reset(value, onlyValue = false)

    override fun resetValueOnly() = resetValueOnly(resetValue)

    override fun resetValueOnly(value: T) = reset(value, onlyValue = true)

    private fun reset(value: T, onlyValue: Boolean) {
        if (!onlyValue) activationControl.reset()
        _valueChangeFlow.update { ValueChange.createResetChange(value) }
    }
}

fun <T> mutableValueControlOf(
    initialValue: T,
    resetValue: T = initialValue,
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableValueControl<T> {
    val activationControl = mutableActivationControlOf(enabled, coroutineScope)
    return MutableValueControlImpl(initialValue, resetValue, activationControl, coroutineScope)
}

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

fun <T> MutableValueControl<List<T>>.reset(values: List<T>) {
    reset(values)
}

fun <T> MutableValueControl<List<T>>.resetValueOnly(vararg values: T) {
    resetValueOnly(values.toList())
}

fun <T> MutableValueControl<List<T>>.resetValueOnly(values: List<T>) {
    resetValueOnly(values)
}
