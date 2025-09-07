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
    val isValidEvents: StateFlow<Boolean>
    val isInvalidEvents: StateFlow<Boolean>
    override val snapshotEvents: StateFlow<ValidatableControlSnapshot<T>>
}

interface MutableValidatableControl<T> : ValidatableControl<T>, MutableValueControl<T>

internal class MutableValidatableValueControlImpl<T>(
    initialValue: T,
    resetValue: T = initialValue,
    private val validators: List<(T) -> String?> = emptyList(),
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
) :
    MutableValueControlImpl<T>(initialValue, resetValue, enabled, coroutineScope),
    MutableValidatableControl<T> {
    private val _validationResultEvents: MutableStateFlow<ValidationResult> =
        MutableStateFlow(ValidationResult.Valid)
    override val validationResultEvents: StateFlow<ValidationResult> =
        _validationResultEvents.asStateFlow()

    override val currentErrorMessage: String?
        get() = _validationResultEvents.value.getErrorMessage()

    override val isValid: Boolean
        get() = _validationResultEvents.value.isValid()

    override val isInvalid: Boolean
        get() = _validationResultEvents.value.isInvalid()

    override val snapshotEvents: StateFlow<ValidatableControlSnapshot<T>> =
        combine(valueChangeEvents, activationStateEvents, validationResultEvents) {
                valueWithTypeChanges,
                enablementState,
                validationResult ->
                ValidatableControlSnapshotImpl.create(
                    valueChangeEvent = valueWithTypeChanges,
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
                        _validationResultEvents.value,
                    ),
            )

    override val isValidEvents: StateFlow<Boolean> =
        validationResultEvents
            .map { x -> x is ValidationResult.Valid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = enabled)

    override val isInvalidEvents: StateFlow<Boolean> =
        validationResultEvents
            .map { x -> x is ValidationResult.Invalid }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = !enabled)

    override fun setValue(value: T) {
        super.setValue(value)
        validate(value)
    }

    override fun validate(value: T) {
        val errorMessage = validators.firstNotNullOfOrNull { validator -> validator(value) }

        _validationResultEvents.update {
            if (errorMessage == null) ValidationResult.Valid
            else ValidationResult.Invalid(errorMessage)
        }
    }
}

fun <T> mutableValidatableControlOf(
    initialValue: T,
    resetValue: T = initialValue,
    validators: List<(T) -> String?> = emptyList(),
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
