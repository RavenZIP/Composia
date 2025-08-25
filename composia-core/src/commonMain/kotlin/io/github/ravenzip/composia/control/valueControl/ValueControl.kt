package io.github.ravenzip.composia.control.valueControl

import io.github.ravenzip.composia.control.extension.stateInDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine

class ValueControl<T>(
    val initialValue: T,
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) : AbstractValueControl<T>(initialValue, disabled, coroutineScope) {
    val snapshotFlow =
        combine(valueWithTypeChangesFlow, statusFlow) { valueWithTypeChanges, status ->
                ValueControlSnapshot.create(
                    valueWithTypeChanges = valueWithTypeChanges,
                    status = status,
                )
            }
            .stateInDefault(
                scope = coroutineScope,
                initialValue = ValueControlSnapshot.createDefault(initialValue),
            )

    val snapshot
        get() = snapshotFlow.value
}
