package io.github.ravenzip.composia.control.activation

import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.state.ActivationState
import io.github.ravenzip.composia.state.activationStatusOf
import io.github.ravenzip.composia.state.isDisabled
import io.github.ravenzip.composia.state.isEnabled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface ActivationControl {
    val activationState: StateFlow<ActivationState>
    val enabledState: StateFlow<Boolean>
    val isEnabled: Boolean
    val isDisabledState: StateFlow<Boolean>
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

    private val _activationState: MutableStateFlow<ActivationState> = MutableStateFlow(initialState)

    override val activationState: StateFlow<ActivationState> = _activationState.asStateFlow()

    override val enabledState: StateFlow<Boolean> =
        activationState
            .map { x -> x is ActivationState.Enabled }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = enabled)

    override val isEnabled: Boolean
        get() = _activationState.value.isEnabled()

    override val isDisabledState: StateFlow<Boolean> =
        activationState
            .map { x -> x is ActivationState.Disabled }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = !enabled)

    override val isDisabled: Boolean
        get() = _activationState.value.isDisabled()

    override fun enable() = _activationState.update { ActivationState.Enabled }

    override fun disable() = _activationState.update { ActivationState.Disabled }

    override fun reset() = _activationState.update { initialState }
}

fun mutableActivationControlOf(
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableActivationControl = MutableActivationControlImpl(enabled, coroutineScope)

fun MutableActivationControl.asReadonly(): ActivationControl = this
