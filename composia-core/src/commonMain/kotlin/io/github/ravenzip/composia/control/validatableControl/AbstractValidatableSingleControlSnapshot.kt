package io.github.ravenzip.composia.control.validatableControl

import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.shared.status.ControlStatus
import io.github.ravenzip.composia.control.singleValueControl.AbstractSingleValueControlSnapshot

abstract class AbstractValidatableSingleControlSnapshot<T>(
    override val value: T,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val status: ControlStatus,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
    open val isValid: Boolean,
    open val isInvalid: Boolean,
    open val errorMessage: String,
) :
    AbstractSingleValueControlSnapshot<T>(
        value = value,
        typeChange = typeChange,
        hasChanges = hasChanges,
        status = status,
        isEnabled = isEnabled,
        isDisabled = isDisabled,
    )
