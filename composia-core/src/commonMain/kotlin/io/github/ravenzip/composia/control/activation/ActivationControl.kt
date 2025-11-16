package io.github.ravenzip.composia.control.activation

import androidx.compose.runtime.Stable
import io.github.ravenzip.composia.extension.stateInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** Контрол, хранящий состояние активации (включено/выключено) */
@Stable
interface ActivationControl {
    /** Поток, который эмиттирует текущее состояние активации ('true', если включен) */
    val isEnabledFlow: StateFlow<Boolean>

    /**
     * Поток, который эмиттирует текущее инвертированное состояние активации на основе
     * [isEnabledFlow] ('true', если выключен)
     */
    val isDisabledFlow: StateFlow<Boolean>

    /** Текущее состояние активации, отражающее последний эмит из [isEnabledFlow] */
    val isEnabled: Boolean

    /**
     * Текущее инвертированное состояние активации, отражающее инвертированное значение последнего
     * эмита из [isEnabledFlow]
     */
    val isDisabled: Boolean
}

/** Изменяемая версия [ActivationControl], позволяющая управлять состоянием активации */
@Stable
interface MutableActivationControl : ActivationControl {
    /** Включает контрол */
    fun enable()

    /** Выключает контрол */
    fun disable()

    /** Сбрасывает состояние контрола до дефолтного, заданного при его создании */
    fun reset()
}

/** Внутренняя реализация [MutableActivationControl] */
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

/**
 * Создаёт изменяемую версию [ActivationControl].
 *
 * @param enabled начальное состояние активации (по умолчанию `true`)
 * @param coroutineScope scope для управления потоками
 */
fun mutableActivationControlOf(
    enabled: Boolean = true,
    coroutineScope: CoroutineScope,
): MutableActivationControl = MutableActivationControlImpl(enabled, coroutineScope)

/** Преобразует [MutableActivationControl] в [ActivationControl]. */
fun MutableActivationControl.asReadonly(): ActivationControl = this
