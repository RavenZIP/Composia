package io.github.ravenzip.composia.control.value

import io.github.ravenzip.composia.control.validatable.ValidatableControlSnapshot
import io.github.ravenzip.composia.control.validatable.ValidatableControlSnapshotImpl
import io.github.ravenzip.composia.validation.ValidationState
import io.github.ravenzip.composia.valueChange.ValueChange
import io.github.ravenzip.composia.valueChange.ValueChangeType
import io.github.ravenzip.composia.valueChange.isInitialize

interface ValueControlSnapshot<T> {
    val value: T
    val typeChange: ValueChangeType
    val hasChanges: Boolean
    val isEnabled: Boolean
    val isDisabled: Boolean
}

internal open class ValueControlSnapshotImpl<T>
private constructor(
    override val value: T,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
) : ValueControlSnapshot<T> {
    companion object {
        fun <T> create(valueChange: ValueChange<T>, isEnabled: Boolean): ValueControlSnapshot<T> =
            ValueControlSnapshotImpl(
                value = valueChange.value,
                typeChange = valueChange.typeChange,
                hasChanges = !valueChange.typeChange.isInitialize(),
                isEnabled = isEnabled,
                isDisabled = !isEnabled,
            )

        fun <T> create(value: T, isEnabled: Boolean): ValueControlSnapshot<T> =
            create(
                valueChange = ValueChange(value = value, typeChange = ValueChangeType.Initialize),
                isEnabled = isEnabled,
            )
    }
}

fun <T> ValueControlSnapshot<T>.toValidatableControlSnapshot(
    validationState: ValidationState
): ValidatableControlSnapshot<T> =
    ValidatableControlSnapshotImpl.create(
        value = value,
        typeChange = typeChange,
        hasChanges = hasChanges,
        isEnabled = isEnabled,
        validationState = validationState,
    )
