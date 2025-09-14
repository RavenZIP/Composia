package io.github.ravenzip.composia.control.value

import io.github.ravenzip.composia.valueChange.ValueChange
import io.github.ravenzip.composia.valueChange.ValueChangeType
import io.github.ravenzip.composia.valueChange.isInitialize

interface ValueControlSnapshot<T> {
    val value: T
    val typeChange: ValueChangeType
    val hasChanges: Boolean
    val isEnabled: Boolean
    val isDisabled: Boolean
}

internal open class ValueControlSnapshotImpl<T>(
    override val value: T,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) : ValueControlSnapshot<T> {
    companion object {
        fun <T> create(valueChange: ValueChange<T>, enabled: Boolean): ValueControlSnapshot<T> =
            ValueControlSnapshotImpl(
                value = valueChange.value,
                typeChange = valueChange.typeChange,
                hasChanges = !valueChange.typeChange.isInitialize(),
                isEnabled = enabled,
                isDisabled = !enabled,
            )

        fun <T> create(value: T, enabled: Boolean): ValueControlSnapshot<T> =
            create(
                valueChange = ValueChange(value = value, typeChange = ValueChangeType.Initialize),
                enabled = enabled,
            )
    }
}
