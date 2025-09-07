package io.github.ravenzip.composia.control.validatable

import io.github.ravenzip.composia.control.value.ValueControlSnapshot
import io.github.ravenzip.composia.state.ActivationState
import io.github.ravenzip.composia.state.isDisabled
import io.github.ravenzip.composia.state.isEnabled
import io.github.ravenzip.composia.validation.ValidationResult
import io.github.ravenzip.composia.validation.getErrorMessage
import io.github.ravenzip.composia.validation.isInvalid
import io.github.ravenzip.composia.validation.isValid
import io.github.ravenzip.composia.valueChange.ValueChangeEvent
import io.github.ravenzip.composia.valueChange.ValueChangeType
import io.github.ravenzip.composia.valueChange.isInitialize

interface ValidatableControlSnapshot<T> : ValueControlSnapshot<T> {
    override val value: T
    override val typeChange: ValueChangeType
    override val hasChanges: Boolean
    override val isEnabled: Boolean
    override val isDisabled: Boolean
    val isValid: Boolean
    val isInvalid: Boolean
    val errorMessage: String?
}

internal open class ValidatableControlSnapshotImpl<T>(
    override val value: T,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
    override val isValid: Boolean,
    override val isInvalid: Boolean,
    override val errorMessage: String?,
) : ValidatableControlSnapshot<T> {
    companion object {
        fun <T> create(
            valueChangeEvent: ValueChangeEvent<T>,
            state: ActivationState,
            validationResult: ValidationResult,
        ): ValidatableControlSnapshot<T> =
            ValidatableControlSnapshotImpl(
                value = valueChangeEvent.value,
                typeChange = valueChangeEvent.typeChange,
                hasChanges = !valueChangeEvent.typeChange.isInitialize(),
                isEnabled = state.isEnabled(),
                isDisabled = state.isDisabled(),
                isValid = validationResult.isValid(),
                isInvalid = validationResult.isInvalid(),
                errorMessage = validationResult.getErrorMessage(),
            )

        fun <T> create(
            value: T,
            state: ActivationState,
            validationResult: ValidationResult,
        ): ValidatableControlSnapshot<T> =
            create(
                valueChangeEvent =
                    ValueChangeEvent(value = value, typeChange = ValueChangeType.Initialize),
                state = state,
                validationResult = validationResult,
            )

        fun <T> createDefault(value: T): ValidatableControlSnapshot<T> =
            create(
                valueChangeEvent =
                    ValueChangeEvent(value = value, typeChange = ValueChangeType.Initialize),
                state = ActivationState.Enabled,
                validationResult = ValidationResult.Valid,
            )
    }
}
