package io.github.ravenzip.composia.control.validatable

import androidx.compose.runtime.Stable
import io.github.ravenzip.composia.control.value.MutableValueControl
import io.github.ravenzip.composia.control.value.ValueControl
import io.github.ravenzip.composia.control.value.mutableValueControlOf
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.status.ControlStatus
import io.github.ravenzip.composia.validation.*
import io.github.ravenzip.composia.valueChange.ValueChangeType
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
    private val validationStateFlow: StateFlow<ValidationState> =
        valueChangeFlow
            .filter { valueChange -> valueChange.typeChange is ValueChangeType.Set }
            .map { valueChange ->
                val errorMessage =
                    validators.firstNotNullOfOrNull { validator -> validator(valueChange.value) }

                if (errorMessage == null) ValidationState.Valid
                else ValidationState.Invalid(errorMessage)
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Eagerly,
                initialValue = ValidationState.Valid,
            )

    override val statusFlow: StateFlow<ControlStatus> =
        combine(valueControl.isEnabledFlow, validationStateFlow) { isEnabled, validationResult ->
                if (isEnabled) validationResult.toControlStatus() else ControlStatus.Disabled
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Eagerly,
                initialValue = ControlStatus.Valid,
            )

    override val errorMessageFlow: StateFlow<String?> =
        validationStateFlow
            .map { value -> value.getErrorMessage() }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = null)

    override val snapshotFlow: StateFlow<ValidatableControlSnapshot<T>> =
        combine(valueControl.snapshotFlow, validationStateFlow) {
                valueControlSnapshot,
                validationResult ->
                ValidatableControlSnapshotImpl.create(
                    value = valueControlSnapshot.value,
                    typeChange = valueControlSnapshot.typeChange,
                    hasChanges = valueControlSnapshot.hasChanges,
                    isEnabled = valueControlSnapshot.isEnabled,
                    validationState = validationResult,
                )
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Eagerly,
                initialValue =
                    ValidatableControlSnapshotImpl.create(
                        valueControl.valueWithTypeChange,
                        valueControl.isEnabled,
                        validationStateFlow.value,
                    ),
            )

    override val isValidFlow: StateFlow<Boolean> =
        validationStateFlow
            .map { x -> x is ValidationState.Valid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = isValid)

    override val isInvalidFlow: StateFlow<Boolean> =
        validationStateFlow
            .map { x -> x is ValidationState.Invalid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = isInvalid)

    override val errorMessage: String?
        get() = validationStateFlow.value.getErrorMessage()

    override val isValid: Boolean
        get() = validationStateFlow.value.isValid()

    override val isInvalid: Boolean
        get() = validationStateFlow.value.isInvalid()

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
