package io.github.ravenzip.composia.control.activation

import androidx.compose.runtime.Stable
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@Stable
interface ActivationControl {
    val isEnabledFlow: StateFlow<Boolean>
    val isDisabledFlow: StateFlow<Boolean>
    val isEnabled: Boolean
    val isDisabled: Boolean
}

@Stable
interface MutableActivationControl : ActivationControl {
    fun enable()

    fun disable()

    fun reset()
}

internal open class MutableActivationControlImpl(
    private val enabled: Boolean = true,
    coroutineScope: CoroutineScope,
) : MutableActivationControl {
    private val _isEnabled: MutableStateFlow<Boolean> = MutableStateFlow(enabled)

    override val isEnabledFlow: StateFlow<Boolean> =
        _isEnabled.stateInWhileSubscribed(scope = coroutineScope, initialValue = enabled)

    override val isEnabled: Boolean
        get() = _isEnabled.value

    override val isDisabledFlow: StateFlow<Boolean> =
        _isEnabled
            .map { isEnabled -> !isEnabled }
            .stateInWhileSubscribed(scope = coroutineScope, initialValue = !enabled)

    override val isDisabled: Boolean
        get() = !_isEnabled.value

    override fun enable() = _isEnabled.update { true }

    override fun disable() = _isEnabled.update { false }

    override fun reset() = _isEnabled.update { enabled }
}

fun mutableActivationControlOf(
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableActivationControl = MutableActivationControlImpl(enabled, coroutineScope)

fun MutableActivationControl.asReadonly(): ActivationControl = this
