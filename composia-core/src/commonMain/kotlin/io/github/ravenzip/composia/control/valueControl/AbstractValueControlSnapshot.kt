package io.github.ravenzip.composia.control.valueControl

import io.github.ravenzip.composia.control.shared.ControlStatus
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.statusControl.AbstractStatusControlSnapshot

abstract class AbstractValueControlSnapshot<T>(
    open val value: T,
    open val typeChange: ValueChangeType,
    open val hasChanges: Boolean,
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) : AbstractStatusControlSnapshot(status, isEnabled, isDisabled)
