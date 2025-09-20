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

@Stable
interface ValidatableControl<T> : ValueControl<T>, Validatable<T> {
    val statusFlow: StateFlow<ControlStatus>
    override val snapshotFlow: StateFlow<ValidatableControlSnapshot<T>>
    val status: ControlStatus
    override val snapshot: ValidatableControlSnapshot<T>
}

@Stable interface MutableValidatableControl<T> : ValidatableControl<T>, MutableValueControl<T>

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

fun <T> MutableValidatableControl<T>.asReadonly(): ValidatableControl<T> = this
