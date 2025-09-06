package io.github.ravenzip.composia.control.multiValueControl

import io.github.ravenzip.composia.control.shared.ValueChangeEvent
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.status.ControlStatus

data class MultiValueControlSnapshot<T>(
    override val value: List<T>,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) :
    AbstractMultiValueControlSnapshot<T>(
        value = value,
        typeChange = typeChange,
        hasChanges = hasChanges,
        status = status,
        isEnabled = isEnabled,
        isDisabled = isDisabled,
    ) {
    companion object {
        fun <T> create(valueWithTypeChanges: ValueChangeEvent<List<T>>, status: ControlStatus) =
            MultiValueControlSnapshot(
                value = valueWithTypeChanges.value,
                typeChange = valueWithTypeChanges.typeChange,
                hasChanges = valueWithTypeChanges.typeChange !is ValueChangeType.Initialize,
                status = status,
                isEnabled = status !is ControlStatus.Disabled,
                isDisabled = status is ControlStatus.Disabled,
            )

        fun <T> create(value: List<T>, status: ControlStatus) =
            create(
                valueWithTypeChanges =
                    ValueChangeEvent(value = value, typeChange = ValueChangeType.Initialize),
                status = status,
            )

        fun <T> createDefault(value: List<T>) = create(value = value, status = ControlStatus.Valid)
    }
}
