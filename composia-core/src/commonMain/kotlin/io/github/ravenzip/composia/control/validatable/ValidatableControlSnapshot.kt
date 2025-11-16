package io.github.ravenzip.composia.control.validatable

import io.github.ravenzip.composia.control.value.ValueControlSnapshot
import io.github.ravenzip.composia.control.value.ValueControlSnapshotImpl
import io.github.ravenzip.composia.validation.ValidationState
import io.github.ravenzip.composia.validation.getErrorMessage
import io.github.ravenzip.composia.validation.isInvalid
import io.github.ravenzip.composia.validation.isValid
import io.github.ravenzip.composia.valueChange.ValueChange
import io.github.ravenzip.composia.valueChange.ValueChangeType

/**
 * Снимок состояния [ValidatableControl] в определённый момент времени.
 *
 * Объединяет в себе текущее значение, тип последнего изменения, валидность и состояние активации
 */
interface ValidatableControlSnapshot<T> : ValueControlSnapshot<T> {
    override val value: T
    override val typeChange: ValueChangeType
    override val hasChanges: Boolean
    override val isEnabled: Boolean
    override val isDisabled: Boolean
    val isValid: Boolean
    val isInvalid: Boolean
    val errorMessage: String?
}

/** Внутренняя реализация [ValidatableControlSnapshot] */
internal open class ValidatableControlSnapshotImpl<T>
private constructor(
    override val value: T,
    override val typeChange: ValueChangeType,
    override val hasChanges: Boolean,
    override val isEnabled: Boolean,
    override val isDisabled: Boolean,
    override val isValid: Boolean,
    override val isInvalid: Boolean,
    override val errorMessage: String?,
) : ValidatableControlSnapshot<T> {
    companion object {
        fun <T> create(
            value: T,
            typeChange: ValueChangeType,
            hasChanges: Boolean,
            isEnabled: Boolean,
            validationState: ValidationState,
        ): ValidatableControlSnapshot<T> =
            ValidatableControlSnapshotImpl(
                value = value,
                typeChange = typeChange,
                hasChanges = hasChanges,
                isEnabled = isEnabled,
                isDisabled = !isEnabled,
                isValid = validationState.isValid(),
                isInvalid = validationState.isInvalid(),
                errorMessage = validationState.getErrorMessage(),
            )

        fun <T> create(
            valueWithTypeChange: ValueChange<T>,
            isEnabled: Boolean,
            validationState: ValidationState,
        ): ValidatableControlSnapshot<T> =
            create(
                value = valueWithTypeChange.value,
                typeChange = valueWithTypeChange.typeChange,
                hasChanges = valueWithTypeChange.typeChange !is ValueChangeType.Initialize,
                isEnabled = isEnabled,
                validationState = validationState,
            )
    }
}

/** Мапинг [ValidatableControlSnapshot] в [ValueControlSnapshot] */
fun <T> ValidatableControlSnapshot<T>.toValidatableControlSnapshot(): ValueControlSnapshot<T> =
    ValueControlSnapshotImpl.create(
        valueChange = ValueChange(value = value, typeChange = typeChange),
        isEnabled = isEnabled,
    )
