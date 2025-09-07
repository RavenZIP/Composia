package io.github.ravenzip.composia.control.value

import io.github.ravenzip.composia.state.ActivationState
import io.github.ravenzip.composia.state.isDisabled
import io.github.ravenzip.composia.state.isEnabled
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
        fun <T> create(
            valueChange: ValueChange<T>,
            state: ActivationState,
        ): ValueControlSnapshot<T> =
            ValueControlSnapshotImpl(
                value = valueChange.value,
                typeChange = valueChange.typeChange,
                hasChanges = !valueChange.typeChange.isInitialize(),
                isEnabled = state.isEnabled(),
                isDisabled = state.isDisabled(),
            )

        fun <T> create(value: T, state: ActivationState): ValueControlSnapshot<T> =
            create(
                valueChange = ValueChange(value = value, typeChange = ValueChangeType.Initialize),
                state = state,
            )

        fun <T> createDefault(value: T): ValueControlSnapshot<T> =
            create(value = value, state = ActivationState.Enabled)
    }
}
