package io.github.ravenzip.composia.control.formControl

import io.github.ravenzip.composia.control.shared.ControlStatus
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.ValueWithTypeChanges

data class CompositeControlSnapshot<T>(
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
    AbstractCompositeControlSnapshot<T>(
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
        fun <T> createDefault(value: T) =
            CompositeControlSnapshot(
                value = value,
                typeChange = ValueChangeType.Initialize,
                hasChanges = false,
                status = ControlStatus.Valid,
                isEnabled = true,
                isDisabled = false,
                isValid = true,
                isInvalid = false,
                errorMessage = "",
            )

        fun <T> create(
            valueWithTypeChanges: ValueWithTypeChanges<T>,
            status: ControlStatus,
            errorMessage: String,
        ) =
            CompositeControlSnapshot(
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
    }
}
