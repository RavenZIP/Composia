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
    val isValidFlow: StateFlow<Boolean>
    val isInvalidFlow: StateFlow<Boolean>
    override val snapshotFlow: StateFlow<ValidatableControlSnapshot<T>>
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
    private val _validationResult: MutableStateFlow<ValidationResult> =
        MutableStateFlow(ValidationResult.Valid)
    override val validationResultFlow: StateFlow<ValidationResult> = _validationResult.asStateFlow()

    override val validationResult: ValidationResult
        get() = _validationResult.value

    override val errorMessage: String?
        get() = _validationResult.value.getErrorMessage()

    override val isValid: Boolean
        get() = _validationResult.value.isValid()

    override val isInvalid: Boolean
        get() = _validationResult.value.isInvalid()

    override val snapshotFlow: StateFlow<ValidatableControlSnapshot<T>> =
        combine(valueChangeFlow, activationStateFlow, validationResultFlow) {
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
                        _validationResult.value,
                    ),
            )

    override val isValidFlow: StateFlow<Boolean> =
        validationResultFlow
            .map { x -> x is ValidationResult.Valid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = enabled)

    override val isInvalidFlow: StateFlow<Boolean> =
        validationResultFlow
            .map { x -> x is ValidationResult.Invalid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = !enabled)

    override fun setValue(value: T) {
        super.setValue(value)
        validate(value)
    }

    override fun validate(value: T) {
        val errorMessage = validators.firstNotNullOfOrNull { validator -> validator(value) }

        _validationResult.update {
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
    val currentValues = this.value.addOrRemove(value, keySelector)

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
