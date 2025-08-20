package io.github.ravenzip.composia.control.formControl

import io.github.ravenzip.composia.ControlStatus
import io.github.ravenzip.composia.ValueChangeType
import io.github.ravenzip.composia.control.valueControl.AbstractValueControlSnapshot

abstract class AbstractFormControlSnapshot<T>(
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
    AbstractValueControlSnapshot<T>(
        value = value,
        typeChange = typeChange,
        hasChanges = hasChanges,
        status = status,
        isEnabled = isEnabled,
        isDisabled = isDisabled,
    )
