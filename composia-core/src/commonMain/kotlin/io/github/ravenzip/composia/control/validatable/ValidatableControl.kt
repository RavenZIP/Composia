package io.github.ravenzip.composia.control.validatable

import io.github.ravenzip.composia.control.value.MutableValueControl
import io.github.ravenzip.composia.control.value.MutableValueControlImpl
import io.github.ravenzip.composia.control.value.ValueControl
import io.github.ravenzip.composia.extension.addOrRemove
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.validation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface ValidatableControl<T> : ValueControl<T>, Validatable<T> {
    val isValidState: StateFlow<Boolean>
    val isInvalidState: StateFlow<Boolean>
    override val snapshotState: StateFlow<ValidatableControlSnapshot<T>>
}

interface MutableValidatableControl<T> : ValidatableControl<T>, MutableValueControl<T>

internal class MutableValidatableValueControlImpl<T>(
    initialValue: T,
    resetValue: T = initialValue,
    private val validators: List<ValidatorFn<T>> = emptyList(),
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
) :
    MutableValueControlImpl<T>(initialValue, resetValue, enabled, coroutineScope),
    MutableValidatableControl<T> {
    private val _validationResultState: MutableStateFlow<ValidationResult> =
        MutableStateFlow(ValidationResult.Valid)
    override val validationResultState: StateFlow<ValidationResult> =
        _validationResultState.asStateFlow()

    override val currentErrorMessage: String?
        get() = _validationResultState.value.getErrorMessage()

    override val isValid: Boolean
        get() = _validationResultState.value.isValid()

    override val isInvalid: Boolean
        get() = _validationResultState.value.isInvalid()

    override val snapshotState: StateFlow<ValidatableControlSnapshot<T>> =
        combine(valueChangeState, activationState, validationResultState) {
                valueWithTypeChanges,
                enablementState,
                validationResult ->
                ValidatableControlSnapshotImpl.create(
                    valueChange = valueWithTypeChanges,
                    state = enablementState,
                    validationResult = validationResult,
                )
            }
            .stateInWhileSubscribed(
                scope = coroutineScope,
                initialValue =
                    ValidatableControlSnapshotImpl.create(
                        initialValue,
                        initialState,
                        _validationResultState.value,
                    ),
            )

    override val isValidState: StateFlow<Boolean> =
        validationResultState
            .map { x -> x is ValidationResult.Valid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = enabled)

    override val isInvalidState: StateFlow<Boolean> =
        validationResultState
            .map { x -> x is ValidationResult.Invalid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = !enabled)

    override fun setValue(value: T) {
        super.setValue(value)
        validate(value)
    }

    override fun validate(value: T) {
        val errorMessage = validators.firstNotNullOfOrNull { validator -> validator(value) }

        _validationResultState.update {
            if (errorMessage == null) ValidationResult.Valid
            else ValidationResult.Invalid(errorMessage)
        }
    }
}

fun <T> mutableValidatableControlOf(
    initialValue: T,
    resetValue: T = initialValue,
    validators: List<ValidatorFn<T>> = emptyList(),
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableValidatableControl<T> =
    MutableValidatableValueControlImpl(
        initialValue,
        resetValue,
        validators,
        enabled,
        coroutineScope,
    )

fun <T> MutableValidatableControl<T>.asReadonly(): ValidatableControl<T> = this

fun <T, K> MutableValidatableControl<List<T>>.toggle(value: T, keySelector: (T) -> K) {
    val currentValues = currentValue.addOrRemove(value, keySelector)

    setValue(currentValues)
    validate(currentValues)
}

fun <T> MutableValidatableControl<List<T>>.setValue(vararg values: T) {
    val newValues = values.toList()

    setValue(newValues)
    validate(newValues)
}

fun <T> MutableValidatableControl<List<T>>.reset(vararg values: T) {
    reset(values.toList())
}
