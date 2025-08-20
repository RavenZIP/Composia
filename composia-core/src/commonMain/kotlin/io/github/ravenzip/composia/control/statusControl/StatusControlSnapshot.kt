package io.github.ravenzip.composia.control.statusControl

import io.github.ravenzip.composia.ControlStatus

data class StatusControlSnapshot(
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) : AbstractStatusControlSnapshot(status, isEnabled, isDisabled) {
    companion object {
        fun createDefault() =
            StatusControlSnapshot(
                status = ControlStatus.Valid,
                isEnabled = true,
                isDisabled = false,
            )
    }
}
