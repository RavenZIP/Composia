package io.github.ravenzip.composia.control.singleValueControl

import io.github.ravenzip.composia.control.shared.ValueChangeEvent
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.status.ControlStatus

data class SingleValueControlSnapshot<T>(
    override val value: T,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) :
    AbstractSingleValueControlSnapshot<T>(
        value = value,
        typeChange = typeChange,
        hasChanges = hasChanges,
        status = status,
        isEnabled = isEnabled,
        isDisabled = isDisabled,
    ) {
    companion object {
        fun <T> create(valueWithTypeChanges: ValueChangeEvent<T>, status: ControlStatus) =
            SingleValueControlSnapshot(
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
                    ValueChangeEvent(value = value, typeChange = ValueChangeType.Initialize),
                status = status,
            )

        fun <T> createDefault(value: T) = create(value = value, status = ControlStatus.Valid)
    }
}
