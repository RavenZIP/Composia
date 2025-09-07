package io.github.ravenzip.composia.control.activation

import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.state.ActivationState
import io.github.ravenzip.composia.state.activationStatusOf
import io.github.ravenzip.composia.state.isDisabled
import io.github.ravenzip.composia.state.isEnabled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface ActivationControl {
    val activationStateEvents: StateFlow<ActivationState>
    val isEnabledEvents: StateFlow<Boolean>
    val isEnabled: Boolean
    val isDisabledEvents: StateFlow<Boolean>
    val isDisabled: Boolean
}

interface MutableActivationControl : ActivationControl {
    fun enable()

    fun disable()

    fun reset()
}

internal open class MutableActivationControlImpl(
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
) : MutableActivationControl {
    internal val initialState: ActivationState = activationStatusOf(enabled)

    private val _activationStateEvents: MutableStateFlow<ActivationState> =
        MutableStateFlow(initialState)

    override val activationStateEvents: StateFlow<ActivationState> =
        _activationStateEvents.asStateFlow()

    override val isEnabledEvents: StateFlow<Boolean> =
        activationStateEvents
            .map { x -> x is ActivationState.Enabled }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = enabled)

    override val isEnabled: Boolean
        get() = _activationStateEvents.value.isEnabled()

    override val isDisabledEvents: StateFlow<Boolean> =
        activationStateEvents
            .map { x -> x is ActivationState.Disabled }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = !enabled)

    override val isDisabled: Boolean
        get() = _activationStateEvents.value.isDisabled()

    override fun enable() = _activationStateEvents.update { ActivationState.Enabled }

    override fun disable() = _activationStateEvents.update { ActivationState.Disabled }

    override fun reset() = _activationStateEvents.update { initialState }
}

fun mutableActivationControlOf(
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableActivationControl = MutableActivationControlImpl(enabled, coroutineScope)

// TODO
// fun MutableEnablementControl.asReadonly(): EnablementControl
