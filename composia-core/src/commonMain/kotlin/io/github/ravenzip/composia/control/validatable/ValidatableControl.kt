package io.github.ravenzip.composia.control.validatable

import androidx.compose.runtime.Stable
import io.github.ravenzip.composia.control.value.MutableValueControl
import io.github.ravenzip.composia.control.value.ValueControl
import io.github.ravenzip.composia.control.value.mutableValueControlOf
import io.github.ravenzip.composia.control.value.toValidatableControlSnapshot
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.status.ControlStatus
import io.github.ravenzip.composia.validation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

/**
 * Контрол, поддерживающий валидацию значения.
 *
 * Расширяет [ValueControl], добавляя информацию о валидности контрола
 */
@Stable
interface ValidatableControl<T> : ValueControl<T>, Validatable<T> {
    /** Поток, эмитирующий текущий статус контрола (валиден, невалиден, выключен) */
    val statusFlow: StateFlow<ControlStatus>

    /** Поток, эмитирующий полный снимок состояния контрола */
    override val snapshotFlow: StateFlow<ValidatableControlSnapshot<T>>

    /** Текущий статус контрола из [statusFlow] */
    val status: ControlStatus

    /** Текущий снимок состояния из [snapshotFlow] */
    override val snapshot: ValidatableControlSnapshot<T>
}

/**
 * Изменяемая версия [ValidatableControl], позволяющая устанавливать значения и менять состояние
 * активации
 */
@Stable interface MutableValidatableControl<T> : ValidatableControl<T>, MutableValueControl<T>

/** Внутренняя реализация [MutableValidatableControl] */
internal class MutableValidatableValueControlImpl<T>(
    private val validators: List<ValidatorFn<T>> = emptyList(),
    valueControl: MutableValueControl<T>,
    coroutineScope: CoroutineScope,
) : MutableValidatableControl<T>, MutableValueControl<T> by valueControl {
    private val _validationStateFlow: StateFlow<ValidationState> =
        valueChangeFlow
            .map { valueChange ->
                val errorMessage =
                    validators.firstNotNullOfOrNull { validator -> validator(valueChange.value) }

                if (errorMessage == null) ValidationState.Valid
                else ValidationState.Invalid(errorMessage)
            }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = ValidationState.Valid)

    override val statusFlow: StateFlow<ControlStatus> =
        combine(valueControl.isEnabledFlow, _validationStateFlow) { isEnabled, validationResult ->
                if (isEnabled) validationResult.toControlStatus() else ControlStatus.Disabled
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Eagerly,
                initialValue = ControlStatus.Valid,
            )

    override val errorMessageFlow: StateFlow<String?> =
        _validationStateFlow
            .map { value -> value.getErrorMessage() }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = null)

    override val snapshotFlow: StateFlow<ValidatableControlSnapshot<T>> =
        combine(valueControl.snapshotFlow, _validationStateFlow) {
                valueControlSnapshot,
                validationState ->
                valueControlSnapshot.toValidatableControlSnapshot(validationState)
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Eagerly,
                initialValue =
                    ValidatableControlSnapshotImpl.create(
                        valueControl.valueWithTypeChange,
                        valueControl.isEnabled,
                        _validationStateFlow.value,
                    ),
            )

    override val isValidFlow: StateFlow<Boolean> =
        _validationStateFlow
            .map { x -> x is ValidationState.Valid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = isValid)

    override val isInvalidFlow: StateFlow<Boolean> =
        _validationStateFlow
            .map { x -> x is ValidationState.Invalid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = isInvalid)

    override val errorMessage: String?
        get() = _validationStateFlow.value.getErrorMessage()

    override val isValid: Boolean
        get() = _validationStateFlow.value.isValid()

    override val isInvalid: Boolean
        get() = _validationStateFlow.value.isInvalid()

    override val status: ControlStatus
        get() = statusFlow.value

    override val snapshot: ValidatableControlSnapshot<T>
        get() = snapshotFlow.value
}

/**
 * Создаёт [MutableValidatableControl] с заданным начальным значением, валидаторами и состоянием
 * активации.
 *
 * @param initialValue начальное значение
 * @param resetValue значение для сброса (по умолчанию [initialValue])
 * @param validators список валидаторов, возвращающих сообщение об ошибке или `null`
 * @param enabled начальное состояние активации (по умолчанию true)
 * @param coroutineScope scope для управления потоками
 */
fun <T> mutableValidatableControlOf(
    initialValue: T,
    resetValue: T = initialValue,
    validators: List<ValidatorFn<T>> = emptyList(),
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableValidatableControl<T> {
    val valueControl =
        mutableValueControlOf(
            initialValue = initialValue,
            resetValue = resetValue,
            enabled = enabled,
            coroutineScope = coroutineScope,
        )

    return MutableValidatableValueControlImpl(validators, valueControl, coroutineScope)
}

/** Преобразует [MutableValidatableControl] в [ValidatableControl] */
fun <T> MutableValidatableControl<T>.asReadonly(): ValidatableControl<T> = this
