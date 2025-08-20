package io.github.ravenzip.composia.control.statusControl

import io.github.ravenzip.composia.ControlStatus

abstract class AbstractStatusControlSnapshot(
    open val status: ControlStatus,
    open val isEnabled: Boolean,
    open val isDisabled: Boolean,
)