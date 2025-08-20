package io.github.ravenzip.composia.control.statusControl

import io.github.ravenzip.composia.ControlStatus
import io.github.ravenzip.composia.control.extension.stateInDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map

class StatusControl(disabled: Boolean = false, coroutineScope: CoroutineScope) :
    AbstractStatusControl(disabled, coroutineScope) {
    val snapshotFlow =
        statusFlow
            .map { x ->
                val isDisabled = x is ControlStatus.Disabled

                StatusControlSnapshot(status = x, isEnabled = !isDisabled, isDisabled = isDisabled)
            }
            .stateInDefault(
                scope = coroutineScope,
                initialValue = StatusControlSnapshot.createDefault(),
            )

    val snapshot
        get() = snapshotFlow.value
}
