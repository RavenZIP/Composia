package io.github.ravenzip.composia.control.enabledControl

import io.github.ravenzip.composia.control.shared.status.EnablementState
import io.github.ravenzip.composia.control.shared.status.enablementStatusOf
import io.github.ravenzip.composia.control.shared.status.isDisabled
import io.github.ravenzip.composia.control.shared.status.isEnabled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface MutableEnablementControl : EnablementControl {
    fun enable()

    fun disable()

    fun reset()
}

internal open class MutableEnablementControlImpl(enabled: Boolean = true) :
    MutableEnablementControl {
    internal val initialState: EnablementState = enablementStatusOf(enabled)

    private val _enablementEvents: MutableStateFlow<EnablementState> =
        MutableStateFlow(initialState)

    override val enablementEvents: StateFlow<EnablementState> = _enablementEvents.asStateFlow()

    override val isEnabled: Boolean
        get() = _enablementEvents.value.isEnabled()

    override val isDisabled: Boolean
        get() = _enablementEvents.value.isDisabled()

    override fun enable() = _enablementEvents.update { EnablementState.Enabled }

    override fun disable() = _enablementEvents.update { EnablementState.Disabled }

    override fun reset() = _enablementEvents.update { initialState }
}

fun mutableEnabledControlOf(enabled: Boolean): MutableEnablementControl =
    MutableEnablementControlImpl(enabled)

fun createSnapshotStateFlow(
    control: EnablementControl,
    initialState: EnablementState,
    coroutineScope: CoroutineScope,
): StateFlow<EnablementControlSnapshot> =
    control.enablementEvents
        .map { status -> EnablementControlSnapshotImpl.create(status) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EnablementControlSnapshotImpl.create(initialState),
        )

// TODO
// fun MutableEnablementControl.asReadonly(): EnablementControl
