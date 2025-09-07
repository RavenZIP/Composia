package io.github.ravenzip.composia.control.valueControl

import io.github.ravenzip.composia.control.shared.ValueChangeEvent
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.isInitialize
import io.github.ravenzip.composia.control.shared.status.ActivationState
import io.github.ravenzip.composia.control.shared.status.isDisabled
import io.github.ravenzip.composia.control.shared.status.isEnabled

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
            valueChangeEvent: ValueChangeEvent<T>,
            state: ActivationState,
        ): ValueControlSnapshot<T> =
            ValueControlSnapshotImpl(
                value = valueChangeEvent.value,
                typeChange = valueChangeEvent.typeChange,
                hasChanges = !valueChangeEvent.typeChange.isInitialize(),
                isEnabled = state.isEnabled(),
                isDisabled = state.isDisabled(),
            )

        fun <T> create(value: T, state: ActivationState): ValueControlSnapshot<T> =
            create(
                valueChangeEvent =
                    ValueChangeEvent(value = value, typeChange = ValueChangeType.Initialize),
                state = state,
            )

        fun <T> createDefault(value: T): ValueControlSnapshot<T> =
            create(value = value, state = ActivationState.Enabled)
    }
}
