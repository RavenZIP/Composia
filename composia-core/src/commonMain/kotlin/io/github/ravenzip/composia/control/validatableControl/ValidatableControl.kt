package io.github.ravenzip.composia.control.validatableControl

import io.github.ravenzip.composia.control.shared.ControlStatus
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.valueControl.AbstractValueControl
import io.github.ravenzip.composia.extension.stateInDefault
import io.github.ravenzip.composia.utils.calculateControlStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class ValidatableControl<T>(
    val initialValue: T,
    private val validators: List<(T) -> String?> = emptyList(),
    val resetValue: T = initialValue,
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) : AbstractValueControl<T>(initialValue, resetValue, disabled, coroutineScope) {
    private val _errorMessage =
        this.valueWithTypeChangesFlow
            .filter { x -> x.typeChange is ValueChangeType.Set }
            .map { x ->
                validators.firstNotNullOfOrNull { validator -> validator(x.value) }.orEmpty()
            }

    private val _statusFlow =
        merge(super.statusFlow, _errorMessage.map { value -> calculateControlStatus(value) })
            .stateInDefault(scope = coroutineScope, initialValue = ControlStatus.Valid)

    val snapshotFlow =
        combine(valueWithTypeChangesFlow, _statusFlow, _errorMessage) {
                valueWithTypeChanges,
                status,
                errorMessage ->
                ValidatableControlSnapshot.create(
                    valueWithTypeChanges = valueWithTypeChanges,
                    status = status,
                    errorMessage = errorMessage,
                )
            }
            .stateInDefault(
                scope = coroutineScope,
                initialValue = ValidatableControlSnapshot.createDefault(initialValue),
            )

    val snapshot
        get() = snapshotFlow.value

    override val statusFlow = _statusFlow

    override val status
        get() = _statusFlow.value

    val errorMessageFlow = _errorMessage.stateInDefault(scope = coroutineScope, initialValue = "")

    val errorMessage
        get() = errorMessageFlow.value

    val isValidFlow =
        _statusFlow
            .map { value -> value is ControlStatus.Valid }
            .stateInDefault(scope = coroutineScope, initialValue = true)

    val isValid
        get() = isValidFlow.value

    val isInvalidFlow =
        _statusFlow
            .map { value -> value is ControlStatus.Invalid }
            .stateInDefault(scope = coroutineScope, initialValue = false)

    val isInvalid
        get() = isInvalidFlow.value
}
