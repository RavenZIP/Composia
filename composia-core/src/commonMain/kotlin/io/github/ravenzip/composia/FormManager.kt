package io.github.ravenzip.composia

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// TODO реализовать isInvalid, isValid, isError, errorMessage, reset
// TODO надо ли отличать типы контролов на пользовательском уровне?
/**
 * [FormManager] - класс для объединения и управления контролами
 *
 * Перед использованием необходимо описать саму форму и ее значения. Форма может содержать внутри
 * себя другие формы.
 *
 * После этого при инициализации формы необходимо указать описанную ранее форму и функции для
 * получения и проставления значения формы
 */
@ExperimentalMultiplatform
class FormManager<T : Any, V : Any>(
    val controls: T,
    formValueProvider: (T) -> V,
    private val setValueProvider: (T, V) -> Unit,
) {
    private val _value = MutableStateFlow(formValueProvider(controls))

    fun setValue(value: V) {
        setValueProvider(controls, value)
    }

    val valueFlow = _value.asStateFlow()

    val value
        get() = _value.value
}
