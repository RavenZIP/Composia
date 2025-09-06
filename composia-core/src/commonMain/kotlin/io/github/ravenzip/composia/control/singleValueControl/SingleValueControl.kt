package io.github.ravenzip.composia.control.singleValueControl

import io.github.ravenzip.composia.extension.stateInDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine

class SingleValueControl<T>(
    val initialValue: T,
    val resetValue: T = initialValue,
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) : AbstractSingleValueControl<T>(initialValue, resetValue, disabled, coroutineScope) {
    val snapshotFlow =
        combine(valueWithTypeChangesFlow, statusFlow) { valueWithTypeChanges, status ->
                SingleValueControlSnapshot.create(
                    valueWithTypeChanges = valueWithTypeChanges,
                    status = status,
                )
            }
            .stateInDefault(
                scope = coroutineScope,
                initialValue = SingleValueControlSnapshot.createDefault(initialValue),
            )

    val snapshot
        get() = snapshotFlow.value
}
