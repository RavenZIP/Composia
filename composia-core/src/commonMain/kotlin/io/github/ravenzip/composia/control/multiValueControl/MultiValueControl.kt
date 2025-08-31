package io.github.ravenzip.composia.control.multiValueControl

import io.github.ravenzip.composia.extension.stateInDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine

class MultiValueControl<T, K>(
    initialValue: List<T>,
    keySelector: (T) -> K,
    resetValue: List<T> = emptyList(),
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) :
    AbstractMultiValueControl<T, K>(
        initialValue = initialValue,
        keySelector = keySelector,
        resetValue = resetValue,
        disabled = disabled,
        coroutineScope = coroutineScope,
    ) {
    val snapshotFlow =
        combine(valueWithTypeChangesFlow, statusFlow) { valueWithTypeChanges, status ->
                MultiValueControlSnapshot.create(
                    valueWithTypeChanges = valueWithTypeChanges,
                    status = status,
                )
            }
            .stateInDefault(
                scope = coroutineScope,
                initialValue = MultiValueControlSnapshot.createDefault(initialValue),
            )

    val snapshot
        get() = snapshotFlow.value
}
