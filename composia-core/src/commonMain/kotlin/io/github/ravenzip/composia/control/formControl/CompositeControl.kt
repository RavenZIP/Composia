package io.github.ravenzip.composia.control.formControl

import io.github.ravenzip.composia.control.extension.stateInDefault
import io.github.ravenzip.composia.control.shared.ControlStatus
import io.github.ravenzip.composia.control.shared.ValueChangeType
import io.github.ravenzip.composia.control.valueControl.AbstractValueControl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class CompositeControl<T>(
    private val initialValue: T,
    private val validators: List<(T) -> String?> = emptyList(),
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) : AbstractValueControl<T>(initialValue, disabled, coroutineScope) {
    private val _errorMessage =
        this.valueWithTypeChangesFlow
            .filter { x -> x.typeChange is ValueChangeType.Set }
            .map { x ->
                validators.firstNotNullOfOrNull { validator -> validator(x.value) }.orEmpty()
            }

    private val _statusFlow =
        merge(
                super.statusFlow,
                _errorMessage.map { value ->
                    if (value.isNotEmpty()) ControlStatus.Invalid(value) else ControlStatus.Valid
                },
            )
            .stateInDefault(scope = coroutineScope, initialValue = ControlStatus.Valid)

    val snapshotFlow =
        combine(valueWithTypeChangesFlow, _statusFlow, _errorMessage) {
                valueWithTypeChanges,
                status,
                errorMessage ->
                CompositeControlSnapshot.create(
                    valueWithTypeChanges = valueWithTypeChanges,
                    status = status,
                    errorMessage = errorMessage,
                )
            }
            .stateInDefault(
                scope = coroutineScope,
                initialValue = CompositeControlSnapshot.createDefault(initialValue),
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
