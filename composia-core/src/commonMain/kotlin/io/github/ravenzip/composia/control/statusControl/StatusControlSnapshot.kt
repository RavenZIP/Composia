package io.github.ravenzip.composia.control.statusControl

import io.github.ravenzip.composia.control.shared.ControlStatus
import io.github.ravenzip.composia.utils.calculateControlStatus

data class StatusControlSnapshot(
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) : AbstractStatusControlSnapshot(status, isEnabled, isDisabled) {
    companion object {
        fun create(isDisabled: Boolean = false) =
            StatusControlSnapshot(
                status = calculateControlStatus(isDisabled),
                isEnabled = !isDisabled,
                isDisabled = isDisabled,
            )

        fun createDefault() =
            StatusControlSnapshot(
                status = ControlStatus.Valid,
                isEnabled = true,
                isDisabled = false,
            )
    }
}
