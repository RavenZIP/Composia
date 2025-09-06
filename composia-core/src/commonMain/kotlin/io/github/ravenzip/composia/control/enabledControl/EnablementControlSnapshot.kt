package io.github.ravenzip.composia.control.enabledControl

import io.github.ravenzip.composia.control.shared.status.EnablementState
import io.github.ravenzip.composia.control.shared.status.isDisabled
import io.github.ravenzip.composia.control.shared.status.isEnabled

interface EnablementControlSnapshot {
    val isEnabled: Boolean
    val isDisabled: Boolean
}

internal open class EnablementControlSnapshotImpl(
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) : EnablementControlSnapshot {
    companion object {
        fun create(status: EnablementState): EnablementControlSnapshot =
            EnablementControlSnapshotImpl(
                isEnabled = status.isEnabled(),
                isDisabled = status.isDisabled(),
            )
    }
}
