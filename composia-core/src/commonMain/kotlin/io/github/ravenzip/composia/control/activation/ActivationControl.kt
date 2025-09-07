package io.github.ravenzip.composia.control.activation

import androidx.compose.runtime.Stable
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import io.github.ravenzip.composia.state.ActivationState
import io.github.ravenzip.composia.state.activationStatusOf
import io.github.ravenzip.composia.state.isDisabled
import io.github.ravenzip.composia.state.isEnabled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

@Stable
interface ActivationControl {
    val activationStateFlow: StateFlow<ActivationState>
    val isEnabledFlow: StateFlow<Boolean>
    val isEnabled: Boolean
    val isDisabledFlow: StateFlow<Boolean>
    val isDisabled: Boolean
}

@Stable
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

    private val _activationStateFlow: MutableStateFlow<ActivationState> =
        MutableStateFlow(initialState)

    override val activationStateFlow: StateFlow<ActivationState> =
        _activationStateFlow.asStateFlow()

    override val isEnabledFlow: StateFlow<Boolean> =
        activationStateFlow
            .map { x -> x is ActivationState.Enabled }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = enabled)

    override val isEnabled: Boolean
        get() = _activationStateFlow.value.isEnabled()

    override val isDisabledFlow: StateFlow<Boolean> =
        activationStateFlow
            .map { x -> x is ActivationState.Disabled }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = !enabled)

    override val isDisabled: Boolean
        get() = _activationStateFlow.value.isDisabled()

    override fun enable() = _activationStateFlow.update { ActivationState.Enabled }

    override fun disable() = _activationStateFlow.update { ActivationState.Disabled }

    override fun reset() = _activationStateFlow.update { initialState }
}

fun mutableActivationControlOf(
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableActivationControl = MutableActivationControlImpl(enabled, coroutineScope)

fun MutableActivationControl.asReadonly(): ActivationControl = this
