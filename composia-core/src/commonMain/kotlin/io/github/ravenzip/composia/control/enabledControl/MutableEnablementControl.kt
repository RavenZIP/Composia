package io.github.ravenzip.composia.control.enabledControl

import io.github.ravenzip.composia.control.shared.status.EnablementState
import io.github.ravenzip.composia.control.shared.status.enablementStatusOf
import io.github.ravenzip.composia.control.shared.status.isDisabled
import io.github.ravenzip.composia.control.shared.status.isEnabled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

interface MutableEnablementControl : EnablementControl {
    fun enable()

    fun disable()

    fun reset()
}

internal open class MutableEnablementControlImpl(enabled: Boolean = true) :
    MutableEnablementControl {
    internal val initialState: EnablementState = enablementStatusOf(enabled)

    private val _enablementFlow: MutableStateFlow<EnablementState> = MutableStateFlow(initialState)

    override val enablementFlow: StateFlow<EnablementState> = _enablementFlow.asStateFlow()

    override val isEnabled: Boolean
        get() = _enablementFlow.value.isEnabled()

    override val isDisabled: Boolean
        get() = _enablementFlow.value.isDisabled()

    override fun enable() = _enablementFlow.update { EnablementState.Enabled }

    override fun disable() = _enablementFlow.update { EnablementState.Disabled }

    override fun reset() = _enablementFlow.update { initialState }
}

fun mutableEnabledControlOf(enabled: Boolean): MutableEnablementControl =
    MutableEnablementControlImpl(enabled)

fun createSnapshotStateFlow(
    control: EnablementControl,
    initialState: EnablementState,
    coroutineScope: CoroutineScope,
): StateFlow<EnablementControlSnapshot> =
    control.enablementFlow
        .map { status -> EnablementControlSnapshotImpl.create(status) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EnablementControlSnapshotImpl.create(initialState),
        )

// TODO
// fun MutableEnablementControl.asReadonly(): EnablementControl
