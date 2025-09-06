package io.github.ravenzip.composia.control.validatableControl

import io.github.ravenzip.composia.control.validation.*
import io.github.ravenzip.composia.control.valueControl.MutableValueControl
import io.github.ravenzip.composia.control.valueControl.MutableValueControlImpl
import io.github.ravenzip.composia.control.valueControl.ValueControl
import io.github.ravenzip.composia.extension.addOrRemove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface ValidatableValueControl<T> : ValueControl<T>, Validatable<T>

interface MutableValidatableValueControl<T> : ValidatableValueControl<T>, MutableValueControl<T>

internal class MutableValidatableValueControlImpl<T>(
    initialValue: T,
    resetValue: T = initialValue,
    private val validators: List<(T) -> String?> = emptyList(),
    enabled: Boolean = true,
) :
    MutableValueControlImpl<T>(initialValue, resetValue, enabled),
    MutableValidatableValueControl<T> {
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

fun <T> mutableValidatableValueControlOf(
    initialValue: T,
    resetValue: T = initialValue,
    validators: List<(T) -> String?> = emptyList(),
    enabled: Boolean,
): MutableValidatableValueControl<T> =
    MutableValidatableValueControlImpl(initialValue, resetValue, validators, enabled)

fun <T> ValidatableValueControl<T>.createIsValidStateFlow(
    initialValue: Boolean = validationResultEvents.value.isValid(),
    coroutineScope: CoroutineScope,
): StateFlow<Boolean> =
    validationResultEvents
        .map { x -> x is ValidationResult.Valid }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialValue,
        )

fun <T> ValidatableValueControl<T>.createIsInvalidStateFlow(
    initialValue: Boolean = validationResultEvents.value.isValid(),
    coroutineScope: CoroutineScope,
): StateFlow<Boolean> =
    validationResultEvents
        .map { x -> x is ValidationResult.Invalid }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialValue,
        )

fun <T, K> MutableValidatableValueControl<List<T>>.toggle(value: T, keySelector: (T) -> K) {
    val currentValues = currentValue.addOrRemove(value, keySelector)

    setValue(currentValues)
    validate(currentValues)
}

fun <T> MutableValidatableValueControl<List<T>>.setValue(vararg values: T) {
    val newValues = values.toList()

    setValue(newValues)
    validate(newValues)
}

fun <T> MutableValidatableValueControl<List<T>>.reset(vararg values: T) {
    reset(values.toList())
}
