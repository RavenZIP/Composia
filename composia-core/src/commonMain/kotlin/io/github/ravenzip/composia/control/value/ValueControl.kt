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

/**
 * Контрол, хранящий значение и тип изменений.
 *
 * Наследуется от [ActivationControl], поэтому также хранит состояния включен\выключен
 */
@Stable
interface ValueControl<T> : ActivationControl {
    /** Поток, эмитирующий значение с типом изменения */
    val valueChangeFlow: StateFlow<ValueChange<T>>

    /** Поток, эмитирующий значение на основе [valueChangeFlow] */
    val valueFlow: StateFlow<T>

    /** Поток, эмитирующий тип последнего изменения значения на основе [valueChangeFlow] */
    val typeChangeFlow: StateFlow<ValueChangeType>

    /** Поток, эмитирующий полный снимок состояния контрола */
    val snapshotFlow: StateFlow<ValueControlSnapshot<T>>

    /** Текущее значение вместе с типом изменения из [valueChangeFlow] */
    val valueWithTypeChange: ValueChange<T>

    /** Текущее значение из [valueChangeFlow] */
    val value: T

    /** Тип последнего изменения из [valueChangeFlow] */
    val typeChange: ValueChangeType

    /** Текущий снимок состояния контрола из [snapshotFlow] */
    val snapshot: ValueControlSnapshot<T>
}

/**
 * Изменяемая версия [ValueControl], позволяющая управлять значением и состоянием активации
 *
 * При создании контрола при помощи [mutableValueControlOf] начальный тип изменений будет
 * [ValueChangeType.Initialize]
 */
@Stable
interface MutableValueControl<T> : ValueControl<T>, MutableActivationControl {
    /** Устанавливает новое значение, создавая изменение типа [ValueChangeType.Set] */
    fun setValue(value: T)

    /**
     * Сбрасывает значение и состояние активации к заданному значению, создавая изменение типа
     * [ValueChangeType.Reset]
     */
    fun reset(value: T)

    /**
     * Сбрасывает только значение, оставляя состояние активации без изменений. Создает изменение
     * типа [ValueChangeType.Reset]
     */
    fun resetValueOnly()

    /**
     * Сбрасывает только значение к заданному, оставляя состояние активации без изменений. Создает
     * изменение типа [ValueChangeType.Reset]
     */
    fun resetValueOnly(value: T)
}

/** Внутренняя реализация [MutableValueControl] */
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

/**
 * Создаёт [MutableValueControl] с заданным начальным значением и состоянием активации
 *
 * @param initialValue начальное значение
 * @param resetValue значение для сброса (по умолчанию [initialValue])
 * @param enabled начальное состояние активации (по умолчанию true)
 * @param coroutineScope scope для управления потоками
 */
fun <T> mutableValueControlOf(
    initialValue: T,
    resetValue: T = initialValue,
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableValueControl<T> {
    val activationControl = mutableActivationControlOf(enabled, coroutineScope)
    return MutableValueControlImpl(initialValue, resetValue, activationControl, coroutineScope)
}

/** Преобразует [MutableValueControl] в [ValueControl] */
fun <T> MutableValueControl<T>.asReadonly(): ValueControl<T> = this

/**
 * Устанавливает новое значение по переданному [keySelector], создавая изменение типа
 * [ValueChangeType.Set]
 */
fun <T, K> MutableValueControl<List<T>>.toggle(value: T, keySelector: (T) -> K) {
    val currentValues = this.value.addOrRemove(value, keySelector)
    setValue(currentValues)
}

/** Устанавливает новое значение, создавая изменение типа [ValueChangeType.Set] */
fun <T> MutableValueControl<List<T>>.setValue(vararg values: T) {
    setValue(values.toList())
}

/**
 * Сбрасывает значение и состояние активации к заданному значению, создавая изменение типа
 * [ValueChangeType.Reset]
 */
fun <T> MutableValueControl<List<T>>.reset(vararg values: T) {
    reset(values.toList())
}

/**
 * Сбрасывает значение и состояние активации к заданному значению, создавая изменение типа
 * [ValueChangeType.Reset]
 */
fun <T> MutableValueControl<List<T>>.reset(values: List<T>) {
    reset(values)
}

/**
 * Сбрасывает только значение к заданному, оставляя состояние активации без изменений. Создает
 * изменение типа [ValueChangeType.Reset]
 */
fun <T> MutableValueControl<List<T>>.resetValueOnly(vararg values: T) {
    resetValueOnly(values.toList())
}

/**
 * Сбрасывает только значение к заданному, оставляя состояние активации без изменений. Создает
 * изменение типа [ValueChangeType.Reset]
 */
fun <T> MutableValueControl<List<T>>.resetValueOnly(values: List<T>) {
    resetValueOnly(values)
}
