package io.github.ravenzip.composia.control

import io.github.ravenzip.composia.ControlStatus
import io.github.ravenzip.composia.ValueChangeType
import io.github.ravenzip.composia.ValueWithTypeChanges
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class FormControl<T>(
    private val initialValue: T,
    private val validators: List<(T) -> String?> = emptyList(),
    disabled: Boolean = false,
    coroutineScope: CoroutineScope,
) : AbstractStatusControl(disabled, coroutineScope) {
    private val _value = MutableStateFlow(ValueWithTypeChanges.createInitializeChange(initialValue))

    private val _errorMessage =
        _value
            .filter { x -> x.typeChange is ValueChangeType.Set }
            .map { x -> validators.firstNotNullOfOrNull { validator -> validator(x.value) } ?: "" }

    private val _status =
        merge(
                super.status,
                _errorMessage.map { value ->
                    if (value.isNotEmpty()) ControlStatus.Invalid(value) else ControlStatus.Valid
                },
            )
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ControlStatus.Valid,
            )

    val value =
        _value
            .map { x -> x.value }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = initialValue,
            )

    val valueWithTypeChanges = _value.asStateFlow()

    override val status = _status

    val errorMessage =
        _errorMessage.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "",
        )

    val isValid =
        _status
            .map { value -> value is ControlStatus.Valid }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = true,
            )

    val isInvalid =
        _status
            .map { value -> value is ControlStatus.Invalid }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false,
            )

    fun setValue(value: T) {
        _value.update { ValueWithTypeChanges.createSetChange(value) }
    }

    override fun reset() {
        super.reset()
        _value.update { ValueWithTypeChanges.createResetChange(initialValue) }
    }
}
