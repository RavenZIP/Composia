package io.github.ravenzip.composia.control.validatableControl

import io.github.ravenzip.composia.control.shared.ControlStatus
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.ValueWithTypeChanges

data class ValidatableSingleControlSnapshot<T>(
    override val value: T,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
    override val isValid: Boolean,
    override val isInvalid: Boolean,
    override val errorMessage: String,
) :
    AbstractValidatableSingleControlSnapshot<T>(
        value = value,
        typeChange = typeChange,
        hasChanges = hasChanges,
        status = status,
        isEnabled = isEnabled,
        isDisabled = isDisabled,
        isValid = isValid,
        isInvalid = isInvalid,
        errorMessage = errorMessage,
    ) {
    companion object {

        fun <T> create(
            valueWithTypeChanges: ValueWithTypeChanges<T>,
            status: ControlStatus,
            errorMessage: String,
        ) =
            ValidatableSingleControlSnapshot(
                value = valueWithTypeChanges.value,
                typeChange = valueWithTypeChanges.typeChange,
                hasChanges = valueWithTypeChanges.typeChange !is ValueChangeType.Initialize,
                status = status,
                isEnabled = status !is ControlStatus.Disabled,
                isDisabled = status is ControlStatus.Disabled,
                isValid = status is ControlStatus.Valid,
                isInvalid = status is ControlStatus.Invalid,
                errorMessage = errorMessage,
            )

        fun <T> create(value: T, status: ControlStatus, errorMessage: String) =
            create(
                valueWithTypeChanges =
                    ValueWithTypeChanges(value = value, typeChange = ValueChangeType.Initialize),
                status = status,
                errorMessage = errorMessage,
            )

        fun <T> createDefault(value: T) =
            create(value = value, status = ControlStatus.Valid, errorMessage = "")
    }
}
