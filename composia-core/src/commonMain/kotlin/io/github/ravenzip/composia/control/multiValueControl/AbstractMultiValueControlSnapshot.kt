package io.github.ravenzip.composia.control.multiValueControl

import io.github.ravenzip.composia.control.shared.status.ControlStatus
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.statusControl.AbstractStatusControlSnapshot

abstract class AbstractMultiValueControlSnapshot<T>(
    open val value: List<T>,
    open val typeChange: ValueChangeType,
    open val hasChanges: Boolean,
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) : AbstractStatusControlSnapshot(status, isEnabled, isDisabled)
