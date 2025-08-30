package io.github.ravenzip.composia.control.valueControl

import io.github.ravenzip.composia.control.shared.ControlStatus
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.ValueWithTypeChanges

data class ValueControlSnapshot<T>(
    override val value: T,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) :
    AbstractValueControlSnapshot<T>(
        value = value,
        typeChange = typeChange,
        hasChanges = hasChanges,
        status = status,
        isEnabled = isEnabled,
        isDisabled = isDisabled,
    ) {
    companion object {
        fun <T> create(valueWithTypeChanges: ValueWithTypeChanges<T>, status: ControlStatus) =
            ValueControlSnapshot(
                value = valueWithTypeChanges.value,
                typeChange = valueWithTypeChanges.typeChange,
                hasChanges = valueWithTypeChanges.typeChange !is ValueChangeType.Initialize,
                status = status,
                isEnabled = status !is ControlStatus.Disabled,
                isDisabled = status is ControlStatus.Disabled,
            )

        fun <T> create(value: T, status: ControlStatus) =
            create(
                valueWithTypeChanges =
                    ValueWithTypeChanges(value = value, typeChange = ValueChangeType.Initialize),
                status = status,
            )

        fun <T> createDefault(value: T) = create(value = value, status = ControlStatus.Valid)
    }
}
